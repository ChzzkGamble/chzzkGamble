package com.chzzkGamble.event;

import org.springframework.context.ApplicationEvent;
import java.util.UUID;

public class AbnormalWebSocketClosedEvent extends ApplicationEvent {

    public AbnormalWebSocketClosedEvent(UUID gambleId) {
        super(gambleId);
    }
}
