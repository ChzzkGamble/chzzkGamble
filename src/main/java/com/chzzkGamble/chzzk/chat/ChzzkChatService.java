package com.chzzkGamble.chzzk.chat;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChzzkChatService {

    private final ChzzkApiService apiService;
    private final Map<String, ChzzkWebSocketClient> socketClientMap = new HashMap<>();

    public ChzzkChatService(ChzzkApiService apiService) {
        this.apiService = apiService;
    }

    public void connectChatRoom(String channelId) {
        ChzzkWebSocketClient socketClient = new ChzzkWebSocketClient(apiService, channelId);
        socketClient.connect("wss://kr-ss2.chat.naver.com/chat");
        socketClientMap.put(channelId, socketClient);
    }

    public void disconnectChatRoom(String channelId) {
        if (!socketClientMap.containsKey(channelId)) {
            throw new RuntimeException("해당 채널과 연결되어 있지 않습니다. " + channelId);
        }
        ChzzkWebSocketClient socketClient = socketClientMap.remove(channelId);
        socketClient.disconnect();
    }
}
