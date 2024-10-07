package com.chzzkGamble.event;

import org.springframework.context.event.EventListener;

class TestEventListener {

    boolean listen = false;

    @EventListener(DonationEvent.class)
    void listen() {
        listen = true;
    }

    boolean isListen() {
        return listen;
    }
}
