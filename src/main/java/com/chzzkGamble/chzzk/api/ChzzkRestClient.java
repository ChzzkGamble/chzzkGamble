package com.chzzkGamble.chzzk.api;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ChzzkRestClient {

    private static final RestClient client = RestClient.create();

    public String get(String url) {
        return client.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
}
