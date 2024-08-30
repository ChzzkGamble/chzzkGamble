package com.chzzkGamble.config;

import jakarta.websocket.ContainerProvider;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {

    public WebSocketConfig() {
        ContainerProvider.getWebSocketContainer()
                .setDefaultMaxBinaryMessageBufferSize(10 * 512 * 1024); // default : 512 * 1024
    }
}
