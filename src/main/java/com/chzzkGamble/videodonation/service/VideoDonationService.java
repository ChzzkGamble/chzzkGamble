package com.chzzkGamble.videodonation.service;

import com.chzzkGamble.chzzk.chat.domain.Chat;
import com.chzzkGamble.chzzk.chat.repository.ChatRepository;
import com.chzzkGamble.videodonation.domain.VideoDonation;
import com.chzzkGamble.videodonation.dto.Criteria;
import com.chzzkGamble.videodonation.dto.VideoDonationRankingResponse;
import com.chzzkGamble.videodonation.dto.VideoDonationRankingResponses;
import com.chzzkGamble.videodonation.repository.VideoDonationRepository;
import com.chzzkGamble.videodonation.youtube.YoutubeClient;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VideoDonationService {

    private final YoutubeClient youtubeClient;
    private final VideoDonationRepository videoDonationRepository;
    private final ChatRepository chatRepository;
    private final Clock clock;

    @Transactional
    public void save(String channelName, int cheese, String msg) {
        String videoId = youtubeClient.getVideoIdByTitleOrNull(msg);
        videoDonationRepository.save(new VideoDonation(channelName, cheese, videoId, msg));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "videoDonation", key = "#channelName", cacheManager = "recentlyCacheManager")
    public List<VideoDonation> getRecentlyVideoDonation(String channelName) {
        Chat chat = chatRepository.findByChannelNameAndOpenedIsTrue(channelName)
                .orElseThrow(() -> new IllegalStateException("최근 연결된 채팅방을 찾을 수 없습니다."));

        LocalDateTime recent = chat.getCreatedAt();

        return videoDonationRepository.findByChannelNameAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(channelName,
                recent);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "videoDonation", key = "#criteria", cacheManager = "rankingCacheManager")
    public VideoDonationRankingResponses getRankingByCriteria(Criteria criteria) {
        LocalDateTime now = LocalDateTime.now(clock);
        List<VideoDonationRankingResponse> ranking = getRanking(criteria, now);

        return new VideoDonationRankingResponses(ranking, now);
    }

    private List<VideoDonationRankingResponse> getRanking(Criteria criteria, LocalDateTime now) {
        PageRequest pageable = PageRequest.of(0, 10);
        if (criteria == Criteria.CHEESE) {
            return videoDonationRepository.findRankingByCheese(now.minusWeeks(1), now, pageable);
        }
        if (criteria == Criteria.COUNT) {
            return videoDonationRepository.findRankingByCount(now.minusWeeks(1), now, pageable);
        }
        if (criteria == Criteria.COMBINED) {
            List<VideoDonationRankingResponse> videoDonations = videoDonationRepository.findVideoDonations(now.minusWeeks(1), now, pageable);
            sortCombinedRanking(videoDonations);
            return videoDonations;

        }

        throw new IllegalArgumentException();
    }

    private void sortCombinedRanking(List<VideoDonationRankingResponse> videoDonations) {
        List<VideoDonationRankingResponse> rankingByCheese = new ArrayList<>(videoDonations);
        List<VideoDonationRankingResponse> rankingByCount = new ArrayList<>(videoDonations);

        rankingByCheese.sort(Comparator.comparing(VideoDonationRankingResponse::getCheese).reversed());
        rankingByCount.sort(Comparator.comparing(VideoDonationRankingResponse::getCount).reversed());

        videoDonations.sort(Comparator.comparing(videoDonation ->
            rankingByCheese.indexOf(videoDonation) + rankingByCount.indexOf(videoDonation)
        ));
    }
}
