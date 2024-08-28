package com.chzzkGamble.chzzk.api;

import com.chzzkGamble.chzzk.dto.ChatInfoApiResponse;
import com.chzzkGamble.chzzk.dto.ChannelInfoApiResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

@Service
public class ChzzkApiService {

    private static final Gson gson = new Gson();
    private final ChzzkRestClient restClient;


    public ChzzkApiService(ChzzkRestClient restClient) {
        this.restClient = restClient;
    }

    public ChannelInfoApiResponse getChannelInfo(String channelId) {
        String url = "https://api.chzzk.naver.com/service/v1/channels/" + channelId;
        String jsonString = restClient.get(url);

        JsonObject content = JsonParser.parseString(jsonString)
                .getAsJsonObject()
                .getAsJsonObject("content");

        return gson.fromJson(content, ChannelInfoApiResponse.class);
    }

    public ChatInfoApiResponse getChatInfo(String channelId) {
        String url = "https://api.chzzk.naver.com/polling/v3/channels/" + channelId + "/live-status";
        String jsonString = restClient.get(url);

        JsonObject content = JsonParser.parseString(jsonString)
                .getAsJsonObject()
                .getAsJsonObject("content");

        return gson.fromJson(content, ChatInfoApiResponse.class);
    }

    public String getChatAccessToken(String chatChannelId) {
        String url = "https://comm-api.game.naver.com/nng_main/v1/chats/access-token?channelId=" + chatChannelId + "&chatType=STREAMING";
        String jsonString = restClient.get(url);

        JsonObject content = JsonParser.parseString(jsonString)
                .getAsJsonObject()
                .getAsJsonObject("content");

        return content.get("accessToken").getAsString();
    }
}
