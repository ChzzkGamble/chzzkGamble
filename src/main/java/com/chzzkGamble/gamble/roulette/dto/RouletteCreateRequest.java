package com.chzzkGamble.gamble.roulette.dto;

import jakarta.validation.constraints.NotBlank;

public record RouletteCreateRequest(
        @NotBlank(message = "channelName is Blank")
        String channelName,

        Integer rouletteUnit
) {

}
