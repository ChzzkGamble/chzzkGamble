package com.chzzkGamble.videodonation.service;

import com.chzzkGamble.chzzk.chat.domain.Chat;
import com.chzzkGamble.chzzk.chat.repository.ChatRepository;
import com.chzzkGamble.videodonation.domain.VideoDonation;
import com.chzzkGamble.videodonation.repository.VideoDonationRepository;
import com.chzzkGamble.videodonation.youtube.YoutubeClient;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VideoDonationService {

    private final YoutubeClient youtubeClient;
    private final VideoDonationRepository videoDonationRepository;
    private final ChatRepository chatRepository;

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
}
