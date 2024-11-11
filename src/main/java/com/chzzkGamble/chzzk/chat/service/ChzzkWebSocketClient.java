package com.chzzkGamble.chzzk.chat.service;

import com.chzzkGamble.chzzk.dto.PingMessage;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.chzzkGamble.utils.MessageSender;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Slf4j
public class ChzzkWebSocketClient {

    private static final String CHZZK_CHAT_SERVER = "wss://kr-ss";
    private static final String CHZZK_CHAT_SERVER2 = ".chat.naver.com/chat";
    private static final int TIMEOUT = 30;
    private static final int MESSAGE_SIZE_LIMIT = 64 * 1024; // 64 KB

    private final WebSocketClient client = new StandardWebSocketClient();
    private final WebSocketHandler handler;
    private final String channelName;

    private WebSocketSession session;

    public ChzzkWebSocketClient(
            WebSocketHandler webSocketHandler,
            String channelName
    ) {
        this.handler = webSocketHandler;
        this.channelName = channelName;
    }

    public void connect() {
        int serverId = ThreadLocalRandom.current().nextInt(1, 6);
        try {
            session = client.execute(handler, CHZZK_CHAT_SERVER + serverId + CHZZK_CHAT_SERVER2)
                    .get(TIMEOUT, TimeUnit.SECONDS);
            session.setTextMessageSizeLimit(MESSAGE_SIZE_LIMIT);
        } catch (TimeoutException | ExecutionException e) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_ERROR, e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_ERROR, e.getMessage());
        }
    }

    public void sendPingToBroadcastServer() {
        if (isConnected()) {
            MessageSender.sendTextMessage(session, new PingMessage());
            log.info("Sent Ping to broadcast server for channelName = {}", channelName);
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
