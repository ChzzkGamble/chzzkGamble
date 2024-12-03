package com.chzzkGamble.videodonation;

import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.chzzkGamble.videodonation.YouTubeApiResponse.Item;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@EnableConfigurationProperties(YoutubeClientConfig.class)
@AllArgsConstructor
public class YoutubeClient {

    private static final RestClient CLIENT = RestClient.create();
    private static final String API_URL_FORMAT =
            "https://www.googleapis.com/youtube/v3/search?part=%s&chart=%s&maxResults=%d&q=%s&type=%s&key=%s";
    private static final String VIDEO_WATCH_URL_FORMAT = "https://www.youtube.com/watch?v=%s";

    private final YoutubeClientConfig config;
    private final YoutubeErrorHandler errorHandler;

    public String getURLByTitle(String title) {
        //TODO: 타임아웃 설정을 해줘야 될 것 같은데, RestClient 전역으로 설정하는게 어떨까요?
        YouTubeApiResponse response = CLIENT.get()
                .uri(String.format(API_URL_FORMAT,
                        config.part(),
                        config.chart(),
                        config.maxResults(),
                        title,
                        config.type(),
                        config.key()))
                .retrieve()
                .onStatus(errorHandler)
                .body(YouTubeApiResponse.class);

        String videoId = extractedVideoId(response, title);
        return String.format(VIDEO_WATCH_URL_FORMAT, videoId);
    }

    private String extractedVideoId(YouTubeApiResponse response, String title) {
        if (response == null) {
            throw new ChzzkException(ChzzkExceptionCode.YOUTUBE_API_INVALID, "유튜브 API 응답 결과가 존재하지 않습니다.");
        }
        Item videoItem = response.items().stream()
                .filter(item -> item.snippet().title().equals(title))
                .findFirst()
                .orElseThrow(() -> new ChzzkException(ChzzkExceptionCode.YOUTUBE_TITLE_INVALID));
        return videoItem.id().videoId();
    }
}

