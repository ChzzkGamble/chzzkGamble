package com.chzzkGamble.chzzk;

import com.chzzkGamble.chzzk.chat.ChzzkChatService;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.chzzkGamble.gamble.domain.Roulette;
import com.chzzkGamble.gamble.service.RouletteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChzzkChatServiceTest {

    private static final String CHANNEL_ID = "0dad8baf12a436f722faa8e5001c5011";
    private Roulette roulette;

    @Autowired
    ChzzkChatService chzzkChatService;

    @Autowired
    RouletteService rouletteService;

    @BeforeEach
    void setUp() {
        roulette = rouletteService.createRoulette(CHANNEL_ID);
    }

    @Test
    @DisplayName("채팅방과 WebScoket 방식으로 연결할 수 있다.")
    void connectChatRoom() {
        assertThatCode(() -> chzzkChatService.connectChatRoom(roulette.getChannelId(), roulette.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 연결된 룰렛은 채팅방과 연결할 수 없다.")
    void connectChatRoom_alreadyConnected_exception() {
        // given
        chzzkChatService.connectChatRoom(roulette.getChannelId(), roulette.getId());

        // when & then
        assertThatThrownBy(() -> chzzkChatService.connectChatRoom(roulette.getChannelId(), roulette.getId()))
                .isInstanceOf(ChzzkException.class)
                .hasMessage(ChzzkExceptionCode.CHAT_IS_CONNECTED.getMessage());
    }

    @Test
    @DisplayName("연결된 채팅방과 연결을 끊을 수 있다.")
    void disconnectChatRoom() {
        // given
        chzzkChatService.connectChatRoom(roulette.getChannelId(), roulette.getId());

        // when & then
        assertThatCode(() -> chzzkChatService.disconnectChatRoom(roulette.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("연결되지 않은 채팅방과 연결을 끊을 수 없다.")
    void disconnectChatRoom_isNotConnected_exception() {
        // given & when & then
        assertThatCode(() -> chzzkChatService.disconnectChatRoom(roulette.getId()))
                .isInstanceOf(ChzzkException.class)
                .hasMessage(ChzzkExceptionCode.CHAT_IS_DISCONNECTED.getMessage());
    }
}
