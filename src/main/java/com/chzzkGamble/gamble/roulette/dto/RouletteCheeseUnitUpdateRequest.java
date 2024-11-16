package com.chzzkGamble.gamble.roulette.dto;

import jakarta.validation.constraints.NotBlank;

public record RouletteCheeseUnitUpdateRequest(
        @NotBlank(message = "룰렛 단위를 입력해주세요.")
        Integer rouletteUnit
) {
}
