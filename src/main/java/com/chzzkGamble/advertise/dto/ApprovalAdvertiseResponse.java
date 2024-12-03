package com.chzzkGamble.advertise.dto;

import com.chzzkGamble.advertise.domain.Advertise;

public record ApprovalAdvertiseResponse(
        Long id,
        String name,
        String imageUrl,
        Double probability
) {
    public static ApprovalAdvertiseResponse of(Advertise advertise, Double probability) {
        return new ApprovalAdvertiseResponse(
                advertise.getId(),
                advertise.getName(),
                advertise.getImageUrl(),
                probability
        );
    }
}
