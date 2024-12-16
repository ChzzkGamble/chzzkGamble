package com.chzzkGamble.videodonation.youtube;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class YoutubeClientConfig {
    private final String part;
    private final String chart;
    private final int maxResults;
    private final String type;
    private final List<String> keys;
    private final AtomicInteger keyIndex;

    public YoutubeClientConfig(
            @Value("${youtube.part}") String part,
            @Value("${youtube.chart}") String chart,
            @Value("${youtube.max-results}") int maxResults,
            @Value("${youtube.type}") String type,
            @Value("${youtube.key}") String key
    ) {
        this.part = part;
        this.chart = chart;
        this.maxResults = maxResults;
        this.type = type;
        this.keys = splitKey(key);
        this.keyIndex = new AtomicInteger(0);
    }

    private List<String> splitKey(String key) {
        return Arrays.stream(key.split(","))
                .map(String::trim)
                .toList();
    }

    public void changeKey() {
        if (keyIndex.get() == keys.size() - 1) {
            throw new IllegalStateException("마지막 api key입니다.");
        }
        keyIndex.getAndIncrement();
    }

    public String part() {
        return part;
    }

    public String chart() {
        return chart;
    }

    public int maxResults() {
        return maxResults;
    }

    public String type() {
        return type;
    }

    public String key() {
        return keys.get(keyIndex.get());
    }
}
