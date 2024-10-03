package com.chzzkGamble.chzzk;

import com.chzzkGamble.chzzk.chat.service.ChzzkChatService;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChzzkChatServiceTest {

    private static final String CHANNEL_NAME = "따효니";

    @Autowired
    ChzzkChatService chzzkChatService;

    @Test
    @DisplayName("채팅방과 WebSocket 방식으로 연결할 수 있다.")
    void connectChatRoom() {
        assertThatCode(() -> chzzkChatService.connectChatRoom(CHANNEL_NAME))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("연결된 채팅방과 연결을 끊을 수 있다.")
    void disconnectChatRoom() {
        // given
        chzzkChatService.connectChatRoom(CHANNEL_NAME);

        // when & then
        assertThatCode(() -> chzzkChatService.disconnectChatRoom(CHANNEL_NAME))
                .doesNotThrowAnyException();
        assertThat(chzzkChatService.isConnected(CHANNEL_NAME)).isFalse();
    }

    @Test
    @DisplayName("연결되지 않은 채팅방과 연결을 끊을 수 없다.")
    void disconnectChatRoom_isNotConnected_exception() {
        // given & when & then
        assertThatCode(() -> chzzkChatService.disconnectChatRoom(CHANNEL_NAME))
                .isInstanceOf(ChzzkException.class)
                .hasMessage(ChzzkExceptionCode.CHAT_IS_DISCONNECTED.getMessage());
    }
}
