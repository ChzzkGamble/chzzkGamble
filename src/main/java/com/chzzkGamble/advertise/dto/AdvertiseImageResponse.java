package com.chzzkGamble.advertise.dto;

import com.chzzkGamble.advertise.domain.Advertise;

public record AdvertiseImageResponse(String url) {

    public static AdvertiseImageResponse from(Advertise advertise) {
        return new AdvertiseImageResponse(advertise.getImageUrl());
    }
}
