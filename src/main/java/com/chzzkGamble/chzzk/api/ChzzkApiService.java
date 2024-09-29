package com.chzzkGamble.chzzk.api;

import com.chzzkGamble.chzzk.dto.ChatInfoApiResponse;
import com.chzzkGamble.chzzk.dto.ChannelInfoApiResponse;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

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

        ChannelInfoApiResponse response = gson.fromJson(content, ChannelInfoApiResponse.class);
        if (response.isInvalid()) {
            throw new ChzzkException(ChzzkExceptionCode.CHANNEL_ID_INVALID, "channelId : " + channelId);
        }
        return response;
    }

    public ChatInfoApiResponse getChatInfo(String channelId) {
        String url = "https://api.chzzk.naver.com/polling/v3/channels/" + channelId + "/live-status";
        String jsonString = restClient.get(url);

        JsonObject content = JsonParser.parseString(jsonString)
                .getAsJsonObject()
                .getAsJsonObject("content");

        return gson.fromJson(content, ChatInfoApiResponse.class);
    }

    public ChatInfoApiResponse getChatInfoByChannelName(String channelName) {
        String url = "https://api.chzzk.naver.com/service/v1/search/channels?keyword=" + channelName;
        String jsonString = restClient.get(url);

        JsonArray data = JsonParser.parseString(jsonString)
                .getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("data");

        if (data.isEmpty()) {
            throw new ChzzkException(ChzzkExceptionCode.CHANNEL_INFO_NOT_FOUND);
        }

        String channelId = data.get(0).getAsJsonObject()
                .getAsJsonObject("channel")
                .getAsJsonPrimitive("channelId")
                .getAsString();

        return getChatInfo(channelId);
    }

    public String getChatAccessToken(String chatChannelId) {
        String url = "https://comm-api.game.naver.com/nng_main/v1/chats/access-token?channelId=" + chatChannelId + "&chatType=STREAMING";
        String jsonString;
        try {
            jsonString = restClient.get(url);
        } catch (HttpServerErrorException.InternalServerError internalServerError) {
            throw new ChzzkException(ChzzkExceptionCode.CHAT_ACCESS_ERROR);
        }

        JsonObject content = JsonParser.parseString(jsonString)
                .getAsJsonObject()
                .getAsJsonObject("content");

        return content.get("accessToken").getAsString();
    }

    public String getChatAccessTokenByChannelName(String channelName) {
        String chatChannelId = getChatInfoByChannelName(channelName).getChatChannelId();
        return getChatAccessToken(chatChannelId);
    }
}
