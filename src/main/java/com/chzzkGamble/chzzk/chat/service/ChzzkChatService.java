package com.chzzkGamble.chzzk.chat.service;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.chat.domain.Chat;
import com.chzzkGamble.chzzk.chat.repository.ChatRepository;
import com.chzzkGamble.event.AbnormalWebSocketClosedEvent;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class ChzzkChatService {

    private static final int MAX_CONNECTION_LIMIT = 10;

    private final ChzzkApiService apiService;
    private final ChatRepository chatRepository;
    private final ApplicationEventPublisher publisher;
    private final Map<UUID, ChzzkWebSocketClient> socketClientMap = new ConcurrentHashMap<>();
    private final Map<String, ChzzkWebSocketClient> chatClients = new ConcurrentHashMap<>();
    private final Set<UUID> tempGambleIds = new ConcurrentSkipListSet<>();

    public ChzzkChatService(ChzzkApiService apiService,
                            ChatRepository chatRepository,
                            ApplicationEventPublisher publisher) {
        this.apiService = apiService;
        this.chatRepository = chatRepository;
        this.publisher = publisher;
    }

    // deprecated
    public void connectChatRoom(String channelName, UUID gambleId) {
        if (tempGambleIds.contains(gambleId)) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_CONNECTING);
        }
        tempGambleIds.add(gambleId);

        if (socketClientMap.containsKey(gambleId)) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_CONNECTED, "gambleId : " + gambleId);
        }
        if (socketClientMap.size() > MAX_CONNECTION_LIMIT) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_LIMIT);
        }

        ChzzkWebSocketClient socketClient = new ChzzkWebSocketClient(apiService, publisher, channelName);
        socketClient.connect();
        socketClientMap.put(gambleId, socketClient);
        tempGambleIds.remove(gambleId);
    }

    @Transactional
    public void connectChatRoom(String channelName) {
        Boolean existsChat = chatRepository.existsByChannelNameAndOpenedIsTrue(channelName);

        if (existsChat) {
            if (chatClients.containsKey(channelName)) {
                // 이미 열려있는 채팅방으로 연결.
                return;
            }

            // TODO 이 부분은 서버가 여러 대일 때, sticky fail로 인해 발생합니다.
            // TODO 각 서버가 현재 어느 채팅방과 연결 중인지 Redis 등을 통해 확인 후 해당 서버로 요청을 보내야 합니다.
            throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_ERROR, "channelName :" + channelName);
        }

        if (chatClients.size() > MAX_CONNECTION_LIMIT) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_LIMIT);
        }
        ChzzkWebSocketClient socketClient = new ChzzkWebSocketClient(apiService, publisher, channelName);
        socketClient.connect();
        chatClients.put(channelName, socketClient);

        Chat chat = new Chat(channelName);
        chat.open();
        chatRepository.save(chat);
    }

    @EventListener(AbnormalWebSocketClosedEvent.class)
    public void reconnectChatRoom(AbnormalWebSocketClosedEvent event) {
        String channelName = (String) event.getSource();
        try {
            chatClients.get(channelName).connect();
        } catch (ChzzkException e) {
            chatClients.remove(channelName);
            throw new ChzzkException(ChzzkExceptionCode.CHAT_RECONNECTION_ERROR);
        }
    }

    // deprecated
    public void disconnectChatRoom(UUID gambleId) {
        if (!socketClientMap.containsKey(gambleId)) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_DISCONNECTED, "gambleId : " + gambleId);
        }
        ChzzkWebSocketClient socketClient = socketClientMap.remove(gambleId);
        socketClient.disconnect();
    }

    @Transactional
    public void disconnectChatRoom(String channelName) {
        if (!chatClients.containsKey(channelName)) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_DISCONNECTED, "channelName : " + channelName);
        }
        ChzzkWebSocketClient socketClient = chatClients.remove(channelName);
        socketClient.disconnect();

        chatRepository.findByChannelNameAndOpenedIsTrue(channelName)
                .ifPresent(Chat::close);
    }

    // deprecated
    public boolean isConnected(UUID gambleId) {
        return socketClientMap.containsKey(gambleId) && socketClientMap.get(gambleId).isConnected();
    }

    public boolean isConnected(String channelName) {
        return chatClients.containsKey(channelName) && chatClients.get(channelName).isConnected();
    }
}
