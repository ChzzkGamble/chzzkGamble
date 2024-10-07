package com.chzzkGamble.gamble.roulette.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RouletteCreateRequest {

    @NotBlank
    private final String channelName;

    @JsonCreator
    public RouletteCreateRequest(@JsonProperty("channelName") String channelName) {
        this.channelName = channelName;
    }
}
