package com.chzzkGamble.config;

import com.chzzkGamble.chzzk.chat.service.ChzzkChatService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
public class GracefulShutdownConfig {

    private final ChzzkChatService chzzkChatService;

    @PreDestroy
    public void shutdown() {
        chzzkChatService.disconnectAll();
    }
}