package com.chzzkGamble.chzzk.chat;

import com.chzzkGamble.chzzk.ChzzkChatCommand;
import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.dto.ConnectionMessage;
import com.chzzkGamble.chzzk.dto.Message;
import com.chzzkGamble.chzzk.dto.PongMessage;
import com.chzzkGamble.gamble.service.RouletteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChzzkSessionHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChzzkApiService apiService;
    private final RouletteService rouletteService;
    private final String channelId;

    public ChzzkSessionHandler(ChzzkApiService apiService, RouletteService rouletteService, String channelId) {
        this.apiService = apiService;
        this.rouletteService = rouletteService;
        this.channelId = channelId;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String chatChannelId = apiService.getChatInfo(channelId).getChatChannelId();
        String chatAccessToken = apiService.getChatAccessToken(chatChannelId);

        ConnectionMessage message = new ConnectionMessage(chatAccessToken, chatChannelId);
        session.sendMessage(new TextMessage(writeAsString(message)));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        ParsedMessage parsedMessage = new ParsedMessage(message);

        // 1. if ping, send pong
        if (ChzzkChatCommand.PING.getNum() == parsedMessage.cmd) {
            session.sendMessage(new TextMessage(writeAsString(new PongMessage())));
        }

        // 2. if donation, send to gamble
        if (ChzzkChatCommand.DONATION.getNum() == parsedMessage.cmd) {
            String msg = parsedMessage.msg;
            int cheese = parsedMessage.cheese;
            rouletteService.vote(channelId, msg, cheese);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private String writeAsString(Message message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("메시지 파싱에 에러가 생겼습니다." + message);
        }
    }

    private static class ParsedMessage {
        int cmd;
        int cheese;
        String msg;

        public ParsedMessage(WebSocketMessage<?> message) {
            JsonObject jsonObject = JsonParser.parseString((String) message.getPayload())
                    .getAsJsonObject();
            this.cmd = jsonObject.getAsJsonObject("cmd").getAsInt();
            this.msg = jsonObject.getAsJsonObject("bdy").get("msg").getAsString();
            this.cheese = parseCheese(jsonObject.getAsJsonObject("bdy").get("extra").getAsString());
        }

        // "{\"isAnonymous\":true,\"payType\":\"CURRENCY\",\"payAmount\":1000,\"donationType\":\"CHAT\",\"weeklyRankList\":[]}"
        private int parseCheese(String extra) {
            int frontIndex = extra.lastIndexOf("payAmount\\\":");
            int backIndex = extra.indexOf(",\\\"donationType");
            return Integer.parseInt(extra.substring(frontIndex, backIndex));
        }
    }
}
