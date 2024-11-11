package com.chzzkGamble.chzzk.dto;

import com.chzzkGamble.chzzk.ChzzkChatCommand;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PingMessage implements Message {

    Integer cmd = ChzzkChatCommand.PING.getNum();
    String ver = "3";
}
