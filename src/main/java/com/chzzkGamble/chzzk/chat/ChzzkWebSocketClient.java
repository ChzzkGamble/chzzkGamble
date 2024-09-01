package com.chzzkGamble.chzzk.chat;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.chzzkGamble.gamble.roulette.service.RouletteService;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

public class ChzzkWebSocketClient {

    private final WebSocketClient client = new StandardWebSocketClient();
    private final WebSocketHandler handler;
    private WebSocketSession session;

    public ChzzkWebSocketClient(ChzzkApiService apiService, RouletteService rouletteService, String channelId, String channelName) {
        this.handler = new ChzzkSessionHandler(apiService, rouletteService, channelId, channelName);
    }

    public void connect(String url) {
        try {
            session = client.execute(handler, url).get(30, TimeUnit.SECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_ERROR, e.getMessage());
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
        return session == null || session.isOpen();
    }
}
