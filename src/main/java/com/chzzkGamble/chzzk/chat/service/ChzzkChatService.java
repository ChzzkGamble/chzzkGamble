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
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChzzkChatService {
    // TODO 새 method 테스트 추가
    private static final int MAX_CONNECTION_LIMIT = 10;

    private final ChzzkApiService apiService;
    private final ChatRepository chatRepository;
    private final ApplicationEventPublisher publisher;
    private final Map<String, ChzzkWebSocketClient> chatClients = new ConcurrentHashMap<>();

    public ChzzkChatService(ChzzkApiService apiService,
                            ChatRepository chatRepository,
                            ApplicationEventPublisher publisher) {
        this.apiService = apiService;
        this.chatRepository = chatRepository;
        this.publisher = publisher;
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
    // TODO : disconnect by time from no roulette exist
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

    public boolean isConnected(String channelName) {
        return chatClients.containsKey(channelName) && chatClients.get(channelName).isConnected();
    }
}
