package com.chzzkGamble.advertise.dto;

import com.chzzkGamble.advertise.domain.Advertise;

public record NotApprovalAdvertiseResponse(
        Long id,
        String name,
        String imageUrl,
        Long cost
) {
    public static NotApprovalAdvertiseResponse of(Advertise advertise) {
        return new NotApprovalAdvertiseResponse(
                advertise.getId(),
                advertise.getName(),
                advertise.getImageUrl(),
                advertise.getCost()
        );
    }
}
