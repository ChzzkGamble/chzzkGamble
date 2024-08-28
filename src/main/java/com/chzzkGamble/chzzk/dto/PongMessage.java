package com.chzzkGamble.chzzk.dto;

import com.chzzkGamble.chzzk.ChzzkChatCommand;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PongMessage implements Message {

    Integer cmd = ChzzkChatCommand.PONG.getNum();
    String ver = "3";
}
