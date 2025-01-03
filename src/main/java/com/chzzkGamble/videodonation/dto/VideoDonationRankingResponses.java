package com.chzzkGamble.videodonation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class VideoDonationRankingResponses {

    private final List<VideoDonationRankingResponse> ranking;
    private final LocalDateTime now;
}
