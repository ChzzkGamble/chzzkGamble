package com.chzzkGamble.chzzk.chat.service;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.dto.Message;
import com.chzzkGamble.chzzk.dto.PingMessage;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Slf4j
public class ChzzkWebSocketClient {

    private static final String CHZZK_CHAT_SERVER = "wss://kr-ss";
    private static final String CHZZK_CHAT_SERVER2 = ".chat.naver.com/chat";

    private final WebSocketClient client = new StandardWebSocketClient();
    private final WebSocketHandler handler;
    private final String channelName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private WebSocketSession session;

    public ChzzkWebSocketClient(
            ChzzkApiService apiService,
            ApplicationEventPublisher publisher,
            String channelName) {
        this.handler = new ChzzkSessionHandler(apiService, publisher, channelName);
        this.channelName = channelName;
    }

    public void connect() {
        int serverId = ThreadLocalRandom.current().nextInt(1, 6);
        try {
            session = client.execute(handler, CHZZK_CHAT_SERVER + serverId + CHZZK_CHAT_SERVER2)
                    .get(30, TimeUnit.SECONDS);
            session.setTextMessageSizeLimit(64 * 1024); // 64 KB
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_ERROR, e.getMessage());
        }
    }

    public void sendPingToBroadcastServer() {
        if (isConnected()) {
            try {
                Message message = new PingMessage();
                session.sendMessage(new TextMessage(writeAsString(message)));
                log.info("Sent Ping to broadcast server for channelName = {}", channelName);
            } catch (IOException e) {
                log.error("Error sending Ping to broadcast server", e);
                throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_ERROR, "Failed to send Ping");
            }
        }
    }

    private String writeAsString(Message message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new ChzzkException(ChzzkExceptionCode.JSON_CONVERT, "message : " + message);
        }
    }

    public void disconnect() {
        if (session == null) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_DISCONNECTED);
        }
        try {
            session.close();
            session = null;
        } catch (IOException e) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_DISCONNECTION_ERROR);
        }
    }

    public boolean isConnected() {
        return session != null && session.isOpen();
    }
}
