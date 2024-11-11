package com.chzzkGamble.gamble.roulette.dto;

import com.chzzkGamble.gamble.roulette.domain.RouletteElement;

public record RouletteElementResponse(
        Long id,
        String name,
        Integer vote,
        String percentage
) {
    public static RouletteElementResponse of(RouletteElement element, Integer totalVote) {
        return new RouletteElementResponse(
                element.getId(),
                element.getName(),
                element.getCount(),
                String.format("%.2f%%", (double) element.getCount() / totalVote * 100)
        );
    }
}
