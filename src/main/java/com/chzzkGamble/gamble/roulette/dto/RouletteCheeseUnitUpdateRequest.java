package com.chzzkGamble.gamble.roulette.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RouletteCheeseUnitUpdateRequest(
        @Positive(message = "룰렛 치즈 단위는 0이상입니다.")
        @NotBlank(message = "룰렛 치즈 단위를 입력해주세요.")
        Integer rouletteUnit
) {
}
