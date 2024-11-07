package com.chzzkGamble.gamble.roulette.dto;

import java.util.List;

public record RouletteResponse(
        List<RouletteElementResponse> elements
) {

}
