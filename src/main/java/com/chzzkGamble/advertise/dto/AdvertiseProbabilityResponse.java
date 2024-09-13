package com.chzzkGamble.advertise.dto;

import com.chzzkGamble.advertise.domain.Advertise;

public record AdvertiseProbabilityResponse(String streamer, Double probability) {

    public static AdvertiseProbabilityResponse from(Advertise advertise, Double probability) {
        return new AdvertiseProbabilityResponse(advertise.getName(), Math.round(probability * 1_000) / 10.0);
    }
}
