package com.chzzkGamble.videodonation.youtube;

import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.chzzkGamble.videodonation.youtube.YouTubeApiResponse.Item;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@EnableConfigurationProperties(YoutubeClientConfig.class)
public class YoutubeClient {

    private static final String API_URL_FORMAT =
            "https://www.googleapis.com/youtube/v3/search?part=%s&chart=%s&maxResults=%d&q=%s&type=%s&key=%s";

    private final RestClient restClient;
    private final YoutubeClientConfig config;
    private final YoutubeErrorHandler errorHandler;

    public YoutubeClient(RestClient.Builder builder, YoutubeClientConfig config, YoutubeErrorHandler errorHandler) {
        this.restClient = builder.build();
        this.config = config;
        this.errorHandler = errorHandler;
    }

    public String getVideoIdByTitle(String title) {
        YouTubeApiResponse response = restClient.get()
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

        return extractedVideoId(response, title);
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

