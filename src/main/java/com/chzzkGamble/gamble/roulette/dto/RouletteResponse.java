package com.chzzkGamble.gamble.roulette.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class RouletteResponse {

    List<RouletteElementResponse> elements;

    public RouletteResponse(List<RouletteElementResponse> elements) {
        this.elements = elements;
    }
}
