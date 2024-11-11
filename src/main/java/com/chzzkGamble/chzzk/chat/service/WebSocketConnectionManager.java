package com.chzzkGamble.chzzk.chat.service;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketConnectionManager {

    private final ChzzkApiService apiService;
    private final ApplicationEventPublisher publisher;
    private final Map<String, ChzzkWebSocketClient> chatClients = new ConcurrentHashMap<>();

    public void connect(String channelName) {
        ChzzkWebSocketClient socketClient = new ChzzkWebSocketClient(apiService, publisher, channelName);
        socketClient.connect();
        chatClients.put(channelName, socketClient);
        log.info("WebSocket connection established for channel '{}'", channelName);
    }

    public void reconnect(String channelName) {
        if (chatClients.containsKey(channelName)) {
            chatClients.get(channelName).connect();
            log.info("Reconnected WebSocket for channel '{}'", channelName);
            return;
        }
        throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_ERROR, "Channel not open: " + channelName);
    }

    public void disconnect(String channelName) {
        ChzzkWebSocketClient client = chatClients.remove(channelName);
        if (client != null) {
            client.disconnect();
            log.info("WebSocket connection closed for channel '{}'", channelName);
        }
    }

    public void sendPingToAllConnections() {
        chatClients.values().forEach(ChzzkWebSocketClient::sendPingToBroadcastServer);
    }

    public int getActiveConnections() {
        return chatClients.size();
    }

    public boolean isConnected(String channelName) {
        return chatClients.containsKey(channelName) && chatClients.get(channelName).isConnected();
    }
}
