package com.chzzkGamble.gamble.dto;

import lombok.Getter;

@Getter
public class RouletteCreateRequest {

    private static final String BASIC_URL = "https://chzzk.naver.com/live/";
    private static final String BASIC_URI = "chzzk.naver.com/live/";

    private final String channelId;

    public RouletteCreateRequest(String streamUri) {
        if (streamUri.startsWith(BASIC_URL)) {
            this.channelId = streamUri.substring(BASIC_URL.length());
        }
        else if (streamUri.startsWith(BASIC_URI)) {
            this.channelId = streamUri.substring(BASIC_URI.length());
        }
        throw new RuntimeException("잘못된 주소입니다.");
    }
}
