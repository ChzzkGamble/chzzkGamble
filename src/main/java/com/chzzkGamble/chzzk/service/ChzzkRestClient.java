package com.chzzkGamble.chzzk.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ChzzkRestClient {

    private static final RestClient client = RestClient.create("https://api.chzzk.naver.com");

    public String get(String additionalUrl) {
        return client.get()
                .uri(additionalUrl)
                .retrieve()
                .body(String.class);
    }
}
