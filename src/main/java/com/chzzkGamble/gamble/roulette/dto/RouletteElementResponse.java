package com.chzzkGamble.gamble.roulette.dto;

import com.chzzkGamble.gamble.roulette.domain.RouletteElement;
import lombok.Getter;

@Getter
public class RouletteElementResponse {

    private Long id;
    private String name;
    private Integer vote;
    private String percentage;

    public RouletteElementResponse(Long id, String name, Integer vote, String percentage) {
        this.id = id;
        this.name = name;
        this.vote = vote;
        this.percentage = percentage;
    }

    public static RouletteElementResponse of(RouletteElement element, Integer totalVote) {
        return new RouletteElementResponse(
                element.getId(),
                element.getName(),
                element.getCount(),
                String.format("%.2f%%", (double) element.getCount() / totalVote * 100)
        );
    }
}
