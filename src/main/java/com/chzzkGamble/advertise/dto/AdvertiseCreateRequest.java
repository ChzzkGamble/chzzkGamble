package com.chzzkGamble.advertise.dto;

import com.chzzkGamble.advertise.domain.Advertise;

public record AdvertiseCreateRequest(String name, String imageUrl, Long cost, Integer adPeriod) {

    public Advertise toEntity() {
        return new Advertise(name, imageUrl, cost, adPeriod);
    }
}
