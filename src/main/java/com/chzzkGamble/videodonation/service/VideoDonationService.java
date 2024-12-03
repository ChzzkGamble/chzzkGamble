package com.chzzkGamble.videodonation.service;

import com.chzzkGamble.videodonation.domain.VideoDonation;
import com.chzzkGamble.videodonation.repository.VideoDonationRepository;
import com.chzzkGamble.videodonation.youtube.YoutubeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VideoDonationService {

    private final YoutubeClient youtubeClient;
    private final VideoDonationRepository videoDonationRepository;

    @Transactional
    public void save(String channelName, int cheese, String msg){
        String videoId = youtubeClient.getVideoIdByTitle(msg);
        videoDonationRepository.save(new VideoDonation(channelName,cheese,videoId));
    }
}
