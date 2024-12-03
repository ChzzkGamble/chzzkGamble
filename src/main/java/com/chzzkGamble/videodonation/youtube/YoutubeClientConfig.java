package com.chzzkGamble.videodonation.youtube;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "youtube")
public record YoutubeClientConfig(
        String part,
        String chart,
        int maxResults,
        String type,
        String key

) {

}
