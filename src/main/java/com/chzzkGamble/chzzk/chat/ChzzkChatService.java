package com.chzzkGamble.chzzk.chat;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.chzzkGamble.gamble.roulette.service.RouletteService;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChzzkChatService {

    private static final String CHZZK_CHAT_SERVER = "wss://kr-ss2.chat.naver.com/chat";
    private static final int MAX_CONNECTION_LIMIT = 10;

    private final ChzzkApiService apiService;
    private final RouletteService rouletteService;
    private final Map<UUID, ChzzkWebSocketClient> socketClientMap = new ConcurrentHashMap<>();

    public ChzzkChatService(ChzzkApiService apiService, RouletteService rouletteService) {
        this.apiService = apiService;
        this.rouletteService = rouletteService;
    }

    public void connectChatRoom(String channelId, UUID gambleId) {
        if (socketClientMap.containsKey(gambleId)) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_CONNECTED, "gambleId : " + gambleId);
        }
        if (socketClientMap.size() > MAX_CONNECTION_LIMIT) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_LIMIT);
        }

        String channelName = apiService.getChannelInfo(channelId).getChannelName();
        ChzzkWebSocketClient socketClient = new ChzzkWebSocketClient(apiService, rouletteService, channelId, channelName);
        socketClient.connect(CHZZK_CHAT_SERVER);
        socketClientMap.put(gambleId, socketClient);
    }

    public void disconnectChatRoom(UUID gambleId) {
        if (!socketClientMap.containsKey(gambleId)) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_IS_DISCONNECTED, "gambleId : " + gambleId);
        }
        ChzzkWebSocketClient socketClient = socketClientMap.remove(gambleId);
        socketClient.disconnect();
    }

    public boolean isConnected(UUID gambleId) {
        return socketClientMap.containsKey(gambleId) && socketClientMap.get(gambleId).isConnected();
    }
}
