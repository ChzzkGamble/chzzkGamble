package com.chzzkGamble.advertise.dto;

import com.chzzkGamble.advertise.domain.Advertise;

public record AdvertiseResponse(String url) {

    public static AdvertiseResponse from(Advertise advertise) {
        return new AdvertiseResponse(advertise.getImageUrl());
    }
}
