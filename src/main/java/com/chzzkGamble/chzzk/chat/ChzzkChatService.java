package com.chzzkGamble.chzzk.chat;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.chzzkGamble.gamble.service.RouletteService;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChzzkChatService {

    public static final String CHZZK_CHAT_SERVER = "wss://kr-ss2.chat.naver.com/chat";

    private final ChzzkApiService apiService;
    private final RouletteService rouletteService;
    private final Map<String, ChzzkWebSocketClient> socketClientMap = new ConcurrentHashMap<>();

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
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_DISCONNECTED, "channelId : " + channelId);
        }
        ChzzkWebSocketClient socketClient = socketClientMap.remove(channelId);
        socketClient.disconnect();
    }
}
