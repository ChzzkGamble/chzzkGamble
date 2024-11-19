package com.chzzkGamble.gamble.roulette.dto;

import jakarta.validation.constraints.Positive;

public record RouletteUnitUpdateRequest(
        @Positive(message = "rouletteUnit must be positive")
        int rouletteUnit
) {

}
