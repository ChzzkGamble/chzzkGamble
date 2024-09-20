package com.chzzkGamble.chzzk.chat;

import com.chzzkGamble.chzzk.ChzzkChatCommand;
import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.dto.ConnectionMessage;
import com.chzzkGamble.chzzk.dto.DonationMessage;
import com.chzzkGamble.chzzk.dto.Message;
import com.chzzkGamble.chzzk.dto.PongMessage;
import com.chzzkGamble.event.AbnormalWebSocketClosedEvent;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import java.util.UUID;

public class ChzzkSessionHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ChzzkApiService chzzkApiService;
    private final ApplicationEventPublisher publisher;
    private final String channelId;
    private final String channelName;
    private final UUID gambleId;

    public ChzzkSessionHandler(ChzzkApiService chzzkApiService,
                               ApplicationEventPublisher publisher,
                               String channelId,
                               String channelName,
                               UUID gambleId) {
        this.publisher = publisher;
        this.chzzkApiService = chzzkApiService;
        this.channelId = channelId;
        this.channelName = channelName;
        this.gambleId = gambleId;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String chatChannelId = chzzkApiService.getChatInfo(channelId).getChatChannelId();
        String chatAccessToken = chzzkApiService.getChatAccessToken(chatChannelId);

        ConnectionMessage message = new ConnectionMessage(chatAccessToken, chatChannelId);
        session.sendMessage(new TextMessage(writeAsString(message)));
        logger.info("connection established : {}", channelName);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        int cmd = parseCmd(message);

        // 1. if ping, send pong
        if (ChzzkChatCommand.PING.getNum() == cmd) {
            session.sendMessage(new TextMessage(writeAsString(new PongMessage())));
            logger.info("PING-PONG with {}", channelName);
        }

        // 2. if donation, send to gamble
        if (ChzzkChatCommand.DONATION.getNum() == cmd) {
            DonationMessage donationMessage = new DonationMessage(channelName, message);
            if (!donationMessage.isDonation()) return;
            logger.info(donationMessage.toString());
            publisher.publishEvent(donationMessage);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        logger.info("connection closed : {}, closeStatus : {}", channelName, closeStatus);
        if (closeStatus.equalsCode(CloseStatus.SERVER_ERROR)) {
            // 서버 측 연결 문제인 경우, 재연결해도 소용없을 확률이 높다.
            return;
        }

        if (!closeStatus.equalsCode(CloseStatus.NORMAL)) {
            // 예기치 못한 이유로 연결이 끊어졌을 때 재연결
            publisher.publishEvent(new AbnormalWebSocketClosedEvent(gambleId));
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private String writeAsString(Message message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new ChzzkException(ChzzkExceptionCode.JSON_CONVERT, "message : " + message);
        }
    }

    private int parseCmd(WebSocketMessage<?> message) {
        return JsonParser.parseString((String) message.getPayload())
                .getAsJsonObject()
                .getAsJsonPrimitive("cmd")
                .getAsInt();
    }
}
