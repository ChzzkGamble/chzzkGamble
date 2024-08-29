package com.chzzkGamble.chzzk.chat;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.gamble.service.RouletteService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChzzkChatService {

    public static final String CHZZK_CHAT_SERVER = "wss://kr-ss2.chat.naver.com/chat";

    private final ChzzkApiService apiService;
    private final RouletteService rouletteService;
    private final Map<String, ChzzkWebSocketClient> socketClientMap = new HashMap<>();

    public ChzzkChatService(ChzzkApiService apiService, RouletteService rouletteService) {
        this.apiService = apiService;
        this.rouletteService = rouletteService;
    }

    public void connectChatRoom(String channelId) {
        ChzzkWebSocketClient socketClient = new ChzzkWebSocketClient(apiService, rouletteService, channelId);
        socketClient.connect(CHZZK_CHAT_SERVER);
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
