package com.chzzkGamble.config;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {

    public WebSocketConfig() {
        WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
        webSocketContainer.setDefaultMaxBinaryMessageBufferSize(100 * 8 * 1024); // default : 8192 Bytes
        webSocketContainer.setDefaultMaxTextMessageBufferSize(100 * 8 * 1024); // default : 8192 Bytes
    }
}
