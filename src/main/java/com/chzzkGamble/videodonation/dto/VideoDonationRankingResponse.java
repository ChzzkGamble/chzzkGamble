package com.chzzkGamble.videodonation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class VideoDonationRankingResponse {

    private final String videoName;
    private final String videoId;
    private final Long cheese;
    private final Long count;
}
