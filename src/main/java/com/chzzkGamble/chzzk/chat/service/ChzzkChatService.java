package com.chzzkGamble.chzzk.chat.service;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.event.AbnormalWebSocketClosedEvent;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class ChzzkChatService {

    private static final int MAX_CONNECTION_LIMIT = 10;

    private final ChzzkApiService apiService;
    private final ApplicationEventPublisher publisher;
    private final Map<UUID, ChzzkWebSocketClient> socketClientMap = new ConcurrentHashMap<>();
    private final Set<UUID> tempGambleIds = new ConcurrentSkipListSet<>();

    public ChzzkChatService(ChzzkApiService apiService, ApplicationEventPublisher publisher) {
        this.apiService = apiService;
        this.publisher = publisher;
    }

    public void connectChatRoom(String channelId, UUID gambleId) {
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

        String channelName = apiService.getChannelInfo(channelId).getChannelName();
        ChzzkWebSocketClient socketClient = new ChzzkWebSocketClient(apiService, publisher, channelName, gambleId);
        socketClient.connect();
        socketClientMap.put(gambleId, socketClient);
        tempGambleIds.remove(gambleId);
    }

    @EventListener(AbnormalWebSocketClosedEvent.class)
    public void reconnectChatRoom(AbnormalWebSocketClosedEvent event) {
        UUID gambleId = (UUID) event.getSource();
        try {
            socketClientMap.get(gambleId).connect();
        } catch (ChzzkException e) {
            socketClientMap.remove(gambleId);
            throw new ChzzkException(ChzzkExceptionCode.CHAT_RECONNECTION_ERROR);
        }
    }

    public void disconnectChatRoom(UUID gambleId) {
        if (!socketClientMap.containsKey(gambleId)) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_DISCONNECTED, "gambleId : " + gambleId);
        }
        ChzzkWebSocketClient socketClient = socketClientMap.remove(gambleId);
        socketClient.disconnect();
    }

    public boolean isConnected(UUID gambleId) {
        return socketClientMap.containsKey(gambleId) && socketClientMap.get(gambleId).isConnected();
    }
}
