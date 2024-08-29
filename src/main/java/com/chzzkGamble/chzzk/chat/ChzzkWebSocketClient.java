package com.chzzkGamble.chzzk.chat;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.gamble.service.RouletteService;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

public class ChzzkWebSocketClient {

    private final WebSocketClient client = new StandardWebSocketClient();
    private final WebSocketHandler handler;
    private WebSocketSession session;

    public ChzzkWebSocketClient(ChzzkApiService apiService, RouletteService rouletteService, String channelId) {
        this.handler = new ChzzkSessionHandler(apiService, rouletteService, channelId);
    }

    public void connect(String url) {
        try {
            session = client.execute(handler, url).get(30, TimeUnit.SECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("채팅방 연결에 실패했습니다.");
        }
    }

    public void disconnect() {
        if (session == null) {
            throw new RuntimeException("연결 상태가 아닙니다.");
        }
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("연결 종료 도중 오류가 발생했습니다.");
        }
    }
}
