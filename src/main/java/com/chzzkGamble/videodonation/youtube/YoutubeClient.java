package com.chzzkGamble.videodonation.youtube;

import com.chzzkGamble.exception.YoutubeException;
import com.chzzkGamble.exception.YoutubeExceptionCode;
import com.chzzkGamble.utils.AsciiEncoder;
import com.chzzkGamble.utils.KJENParser;
import com.chzzkGamble.videodonation.youtube.YouTubeApiResponse.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.net.URI;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class YoutubeClient {

    private static final String API_URL_FORMAT =
            "https://www.googleapis.com/youtube/v3/search?part=%s&chart=%s&maxResults=%d&q=%s&type=%s&key=%s";
    private static final int MAX_TITLE_LENGTH = 50;

    private final RestClient restClient;
    private final YoutubeClientConfig config;
    private final YoutubeErrorHandler errorHandler;
    private final ReentrantLock lock = new ReentrantLock();

    public YoutubeClient(RestClient.Builder builder, YoutubeClientConfig config, YoutubeErrorHandler errorHandler) {
        this.restClient = builder.build();
        this.config = config;
        this.errorHandler = errorHandler;
    }

    public String getVideoIdByTitleOrNull(String title) {
        lock.lock();
        try {
            return getVideoIdByTitle(title);
        } catch (YoutubeException e) {
            if (e.getExceptionCode() == YoutubeExceptionCode.YOUTUBE_QUOTA_EXCEEDED) {
                config.changeKey();
                return getVideoIdByTitleOrNull(title);
            }

            log.error(e.getMessage());
            log.error(e.getSupplementaryMessage());
        } finally {
            lock.unlock();
        }
        return null;
    }

    private String getVideoIdByTitle(String title) {
        YouTubeApiResponse response = restClient.get()
                .uri(getUri(title))
                .retrieve()
                .onStatus(errorHandler)
                .body(YouTubeApiResponse.class);

        return extractedVideoId(response, title);
    }

    private URI getUri(String title) {
        return URI.create(
                String.format(API_URL_FORMAT,
                    config.part(),
                    config.chart(),
                    config.maxResults(),
                    AsciiEncoder.encode(KJENParser.extractKJEN(title), MAX_TITLE_LENGTH),
                    config.type(),
                    config.key())
        );
    }

    private String extractedVideoId(YouTubeApiResponse response, String title) {
        if (response == null) {
            throw new YoutubeException(YoutubeExceptionCode.YOUTUBE_API_INVALID, "유튜브 API 응답 결과가 존재하지 않습니다.");
        }
        Item videoItem = response.items().stream()
                .filter(item -> item.snippet().title().equals(title))
                .findFirst()
                .orElseThrow(() -> new YoutubeException(YoutubeExceptionCode.YOUTUBE_TITLE_INVALID, response.toString()));
        return videoItem.id().videoId();
    }
}

