package com.chzzkGamble.videodonation.dto;

import com.chzzkGamble.utils.Ranking;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class VideoDonationRankingResponses {

    private final List<Ranking<VideoDonationRankingResponse>> ranking;
    private final LocalDateTime now;

    public int getRank(VideoDonationRankingResponse videoDonation) {
        for (Ranking<VideoDonationRankingResponse> rankingResponse : ranking) {
            if (rankingResponse.getElement().equals(videoDonation)) {
                return rankingResponse.getRank();
            }
        }
        throw new IllegalArgumentException("해당 요소가 존재하지 않습니다.");
    }
}
