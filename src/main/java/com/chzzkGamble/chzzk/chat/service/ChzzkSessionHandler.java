package com.chzzkGamble.chzzk.chat.service;

import com.chzzkGamble.chzzk.ChzzkChatCommand;
import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.dto.ConnectionMessage;
import com.chzzkGamble.chzzk.dto.DonationMessage;
import com.chzzkGamble.chzzk.dto.PongMessage;
import com.chzzkGamble.event.AbnormalWebSocketClosedEvent;
import com.chzzkGamble.event.DonationEvent;
import com.chzzkGamble.utils.MessageSender;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@RequiredArgsConstructor
public class ChzzkSessionHandler implements WebSocketHandler {

    private final ChzzkApiService chzzkApiService;
    private final ApplicationEventPublisher publisher;
    private final String channelName;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String chatChannelId = chzzkApiService.getChatInfo(channelName).getChatChannelId();
        String chatAccessToken = chzzkApiService.getChatAccessToken(chatChannelId);

        ConnectionMessage message = new ConnectionMessage(chatAccessToken, chatChannelId);
        MessageSender.sendTextMessage(session, message);
        log.info("connection established : {}", channelName);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        int cmd = parseCmd(message);

        if (ChzzkChatCommand.PING.getNum() == cmd) {
            MessageSender.sendTextMessage(session, new PongMessage());
            log.info("PING-PONG with {}", channelName);
        }

        if (ChzzkChatCommand.PONG.getNum() == cmd) {
            log.info("PONG with {}", channelName);
        }

        if (ChzzkChatCommand.DONATION.getNum() == cmd) {
            DonationMessage donationMessage = new DonationMessage(channelName, message);
            if (!donationMessage.isDonation()) {
                return;
            }
            log.info(donationMessage.toString());
            publisher.publishEvent(new DonationEvent(donationMessage));
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
        log.info("connection closed : {}, closeStatus : {}", channelName, closeStatus);
        if (closeStatus.equalsCode(CloseStatus.SERVER_ERROR)) {
            // 서버 측 연결 문제인 경우, 재연결해도 소용없을 확률이 높다.
            return;
        }

        if (!closeStatus.equalsCode(CloseStatus.NORMAL)) {
            // 예기치 못한 이유로 연결이 끊어졌을 때 재연결
            publisher.publishEvent(new AbnormalWebSocketClosedEvent(channelName));
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
