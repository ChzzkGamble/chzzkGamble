package com.chzzkGamble.advertise.dto;

import com.chzzkGamble.advertise.domain.Advertise;

import java.time.LocalDateTime;

public record ApprovalAdvertiseResponse(
        Long id,
        String name,
        String imageUrl,
        Long cost,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static ApprovalAdvertiseResponse of(Advertise advertise) {
        return new ApprovalAdvertiseResponse(
                advertise.getId(),
                advertise.getName(),
                advertise.getImageUrl(),
                advertise.getCost(),
                advertise.getStartDate(),
                advertise.getEndDate()
        );
    }
}
