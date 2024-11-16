package com.chzzkGamble.gamble.roulette.dto;

import com.chzzkGamble.gamble.roulette.domain.RouletteElement;

public record RouletteElementResponse(
        Long id,
        String name,
        Integer vote,
        String percentage
) {
    public static RouletteElementResponse of(RouletteElement element, int cheeseUnit, Integer totalVote) {
        return new RouletteElementResponse(
                element.getId(),
                element.getName(),
                element.getCheese() / cheeseUnit,
                String.format("%.2f%%", (double) (element.getCheese() / cheeseUnit) / totalVote * 100)
        );
    }
}
