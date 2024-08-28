package com.chzzkGamble.chzzk.service.chat;

import com.chzzkGamble.chzzk.service.api.ChzzkApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChzzkSessionHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChzzkApiService apiService;
    private final String channelId;

    public ChzzkSessionHandler(ChzzkApiService apiService, String channelId) {
        this.apiService = apiService;
        this.channelId = channelId;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String chatChannelId = apiService.getChatInfo(channelId).getChatChannelId();
        String chatAccessToken = apiService.getChatAccessToken(chatChannelId);

        ConnectionMessage message = new ConnectionMessage(chatAccessToken, chatChannelId);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        System.out.println(message.getPayload());
        System.out.println("-------------------");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
