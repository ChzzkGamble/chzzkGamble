package com.chzzkGamble.chzzk.chat;

import com.chzzkGamble.chzzk.ChzzkChatCommand;
import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.dto.ConnectionMessage;
import com.chzzkGamble.chzzk.dto.Message;
import com.chzzkGamble.chzzk.dto.PongMessage;
import com.chzzkGamble.gamble.service.RouletteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.util.StringUtils;
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
        int cmd = parseCmd(message);

        // 1. if ping, send pong
        if (ChzzkChatCommand.PING.getNum() == cmd) {
            session.sendMessage(new TextMessage(writeAsString(new PongMessage())));
        }

        // 2. if donation, send to gamble
        if (ChzzkChatCommand.DONATION.getNum() == cmd) {
            DonationMessage donationMessage = new DonationMessage(message);
            String msg = donationMessage.msg;
            int cheese = donationMessage.cheese;
            rouletteService.vote(channelId, msg, cheese);
        }
    }

    private int parseCmd(WebSocketMessage<?> message) {
        return JsonParser.parseString((String) message.getPayload())
                .getAsJsonObject()
                .getAsJsonPrimitive("cmd")
                .getAsInt();
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

    private static class DonationMessage {
        int cheese;
        String msg;

        public DonationMessage(WebSocketMessage<?> message) {
            JsonArray bdy = JsonParser.parseString((String) message.getPayload())
                    .getAsJsonObject()
                    .getAsJsonArray("bdy");
            for (int i = 0; i < bdy.size(); i++) {
                JsonObject jsonObject = bdy.get(i).getAsJsonObject();
                if (jsonObject.get("msg") != null) {
                    this.msg = jsonObject.get("msg").getAsString();
                }
                if (jsonObject.get("extras") != null) {
                    this.cheese = parseCheese(jsonObject.get("extras").getAsJsonPrimitive().getAsString());
                }
            }
        }

        private static int parseCheese(String extras) {
            for (String s : StringUtils.commaDelimitedListToSet(extras)) {
                if (s.contains("payAmount")) {
                    return Integer.parseInt(s.split(":")[1]);
                }
            }
            throw new RuntimeException("치즈 파싱에 실패했습니다. " + extras);
        }

        @Override
        public String toString() {
            return "DonationMessage{" +
                    "cheese=" + cheese +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }
}
