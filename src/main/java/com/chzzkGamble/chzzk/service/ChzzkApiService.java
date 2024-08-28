package com.chzzkGamble.chzzk.service;

import com.chzzkGamble.chzzk.dto.ChannelInfoApiResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

@Service
public class ChzzkApiService {

    private static final Gson gson = new Gson();
    private final ChzzkRestClient client;

    public ChzzkApiService(ChzzkRestClient client) {
        this.client = client;
    }

    public ChannelInfoApiResponse getChannelInfo(String channelId) {
        String additionalUrl = "/service/v1/channels/" + channelId;
        String jsonString = client.get(additionalUrl);

        JsonObject content = JsonParser.parseString(jsonString)
                .getAsJsonObject()
                .getAsJsonObject("content");

        return gson.fromJson(content, ChannelInfoApiResponse.class);
    }
}
