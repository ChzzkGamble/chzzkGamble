package com.chzzkGamble.chzzk.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatConnectRequest {

    @NotBlank
    private final String channelName;

    @JsonCreator
    public ChatConnectRequest(@JsonProperty("channelName") String channelName) {
        this.channelName = channelName;
    }
}
