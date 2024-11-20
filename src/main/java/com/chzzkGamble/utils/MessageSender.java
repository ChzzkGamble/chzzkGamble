package com.chzzkGamble.utils;

import com.chzzkGamble.chzzk.dto.Message;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSender {

    public static void sendTextMessage(WebSocketSession session, Message message) {
        try {
            String jsonString = writeAsString(message);
            session.sendMessage(new TextMessage(jsonString));
        } catch (IOException e) {
            log.error("Error sending message", e);
            throw new ChzzkException(ChzzkExceptionCode.CHAT_CONNECTION_ERROR);
        }
    }

    private static String writeAsString(Message message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new ChzzkException(ChzzkExceptionCode.JSON_CONVERT, "Failed to convert message to JSON");
        }
    }
}
