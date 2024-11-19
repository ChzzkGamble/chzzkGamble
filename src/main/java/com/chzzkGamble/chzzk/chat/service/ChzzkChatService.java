package com.chzzkGamble.chzzk.chat.service;

import com.chzzkGamble.chzzk.chat.domain.Chat;
import com.chzzkGamble.chzzk.chat.repository.ChatRepository;
import com.chzzkGamble.chzzk.dto.DonationMessage;
import com.chzzkGamble.event.AbnormalWebSocketClosedEvent;
import com.chzzkGamble.event.DonationEvent;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChzzkChatService {

    private static final int MAX_CONNECTION_LIMIT = 10;
    private static final int CHAT_ALIVE_MINUTES = 10;

    private final ChatRepository chatRepository;
    private final Clock clock;
    private final WebSocketConnectionManager connectionManager;
    private final Map<String, LocalDateTime> lastEventPublished = new ConcurrentHashMap<>();

    // TODO : 동시성 제어 문제 해결 -> channelName 네임드락
    @Transactional
    public void connectChatRoom(String channelName) {
        if (isChatAlreadyOpen(channelName) && connectionManager.isConnected(channelName)) {
            lastEventPublished.put(channelName, LocalDateTime.now(clock));
            return;
        }

        validateConnectionLimit();
        initiateNewChatConnection(channelName);
    }

    private boolean isChatAlreadyOpen(String channelName) {
        return chatRepository.existsByChannelNameAndOpenedIsTrue(channelName);
    }

    private void validateConnectionLimit() {
        if (connectionManager.getActiveConnections() >= MAX_CONNECTION_LIMIT) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_LIMIT);
        }
    }

    private void initiateNewChatConnection(String channelName) {
        connectionManager.connect(channelName);
        lastEventPublished.put(channelName, LocalDateTime.now(clock));
        createNewChat(channelName);
    }

    private void createNewChat(String channelName) {
        Chat chat = new Chat(channelName);
        chat.open();
        chatRepository.save(chat);
    }

    @Scheduled(fixedRateString = "${client.ping-interval:30000}")
    public void sendPingToBroadcastServers() {
        connectionManager.sendPingToAllConnections();
    }

    @EventListener(AbnormalWebSocketClosedEvent.class)
    void reconnectChatRoom(AbnormalWebSocketClosedEvent event) {
        String channelName = (String) event.getSource();
        connectionManager.reconnect(channelName);
    }

    @EventListener(DonationEvent.class)
    void updateLastEventTime(DonationEvent donationEvent) {
        DonationMessage donationMessage = (DonationMessage) donationEvent.getSource();
        String channelName = donationMessage.getChannelName();

        lastEventPublished.put(channelName, LocalDateTime.now(clock));
    }

    @Transactional
    @Scheduled(fixedDelayString = "${chat.close-interval}")
    public void disconnectChatRoom() {
        List<String> inactiveChannels = getInactiveChannels();

        inactiveChannels.forEach(channel -> {
            disconnectChatRoom(channel);
            lastEventPublished.remove(channel);
        });
    }

    private List<String> getInactiveChannels() {
        return lastEventPublished.entrySet().stream()
                .filter(entry -> entry.getValue().isBefore(LocalDateTime.now(clock).minusMinutes(CHAT_ALIVE_MINUTES)))
                .map(Map.Entry::getKey)
                .toList();
    }

    private void disconnectChatRoom(String channelName) {
        connectionManager.disconnect(channelName);
        chatRepository.findByChannelNameAndOpenedIsTrue(channelName).ifPresent(Chat::close);
        log.info("connection with {} is closed by timeout", channelName);
    }

    public boolean isConnected(String channelName) {
        return connectionManager.isConnected(channelName);
    }
}
