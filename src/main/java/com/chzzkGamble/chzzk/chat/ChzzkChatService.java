package com.chzzkGamble.chzzk.chat;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import org.springframework.stereotype.Service;

@Service
public class ChzzkChatService {

    private final ChzzkApiService apiService;

    public ChzzkChatService(ChzzkApiService apiService) {
        this.apiService = apiService;
    }

    public void connectChatRoom(String channelId) {
        ChzzkWebSocketClient socketClient = new ChzzkWebSocketClient(apiService, channelId);
        socketClient.connect("wss://kr-ss2.chat.naver.com/chat");
    }
}
