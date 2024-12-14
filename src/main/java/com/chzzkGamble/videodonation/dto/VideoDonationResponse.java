package com.chzzkGamble.videodonation.dto;

import com.chzzkGamble.videodonation.domain.VideoDonation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class VideoDonationResponse {

    private final String videoName;
    private final String videoId;
    private final Integer cheese;
    private final LocalDateTime createdAt;

    public static VideoDonationResponse of(VideoDonation videoDonation) {
        return new VideoDonationResponse(
                videoDonation.getVideoName(),
                videoDonation.getVideoId(),
                videoDonation.getCheese(),
                videoDonation.getCreatedAt()
        );
    }
}
