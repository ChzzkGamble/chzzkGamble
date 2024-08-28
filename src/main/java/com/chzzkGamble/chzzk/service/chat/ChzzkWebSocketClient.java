package com.chzzkGamble.chzzk.service.chat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import com.chzzkGamble.chzzk.service.api.ChzzkApiService;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

public class ChzzkWebSocketClient {

    private final WebSocketClient client = new StandardWebSocketClient();
    private final WebSocketHandler handler;

    public ChzzkWebSocketClient(ChzzkApiService apiService, String channelId) {
        this.handler = new ChzzkSessionHandler(apiService, channelId);
    }

    public void connect(String url) {
        try {
            client.execute(handler, url).get(30, TimeUnit.SECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("채팅방 연결에 실패했습니다.");
        }
    }
}
