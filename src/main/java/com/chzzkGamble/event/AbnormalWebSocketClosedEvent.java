package com.chzzkGamble.event;

import org.springframework.context.ApplicationEvent;

public class AbnormalWebSocketClosedEvent extends ApplicationEvent {

    public AbnormalWebSocketClosedEvent(String channelName) {
        super(channelName);
    }
}
