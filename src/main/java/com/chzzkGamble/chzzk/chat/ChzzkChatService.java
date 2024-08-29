package com.chzzkGamble.chzzk.chat;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.chzzkGamble.gamble.service.RouletteService;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChzzkChatService {

    public static final String CHZZK_CHAT_SERVER = "wss://kr-ss2.chat.naver.com/chat";

    private final ChzzkApiService apiService;
    private final RouletteService rouletteService;
    private final Map<UUID, ChzzkWebSocketClient> socketClientMap = new ConcurrentHashMap<>();

    public ChzzkChatService(ChzzkApiService apiService, RouletteService rouletteService) {
        this.apiService = apiService;
        this.rouletteService = rouletteService;
    }

    public void connectChatRoom(String channelId, UUID rouletteId) {
        if (socketClientMap.containsKey(rouletteId)) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_CONNECTED, "rouletteId : " + rouletteId);
        }

        ChzzkWebSocketClient socketClient = new ChzzkWebSocketClient(apiService, rouletteService, channelId);
        socketClient.connect(CHZZK_CHAT_SERVER);
        socketClientMap.put(rouletteId, socketClient);
    }

    public void disconnectChatRoom(UUID rouletteId) {
        if (!socketClientMap.containsKey(rouletteId)) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_DISCONNECTED, "rouletteId : " + rouletteId);
        }
        ChzzkWebSocketClient socketClient = socketClientMap.remove(rouletteId);
        socketClient.disconnect();
    }

    public boolean isConnected(UUID rouletteId) {
        return !socketClientMap.containsKey(rouletteId) || socketClientMap.get(rouletteId).isConnected();
    }
}
