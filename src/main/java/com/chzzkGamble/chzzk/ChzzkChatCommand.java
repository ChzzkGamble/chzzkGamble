package com.chzzkGamble.chzzk;

import lombok.Getter;

@Getter
public enum ChzzkChatCommand {

    PING(0),
    PONG(10000),
    CONNECT(100),
    SEND_CHAT(3101),
    REQUEST_RECENT_CHAT(5101),
    CHAT(93101),
    DONATION(93102);

    private final int num;

    ChzzkChatCommand(int num) {
        this.num = num;
    }
}
