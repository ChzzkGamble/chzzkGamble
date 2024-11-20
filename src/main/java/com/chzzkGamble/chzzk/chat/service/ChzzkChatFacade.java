package com.chzzkGamble.chzzk.chat.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChzzkChatFacade {

    private final ChzzkChatService chzzkChatService;
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    public void connectChatRoom(String channelName) {
        locks.putIfAbsent(channelName, new Object());

        synchronized (locks.get(channelName)) {
            chzzkChatService.connectChatRoom(channelName);
        }
    }
}
