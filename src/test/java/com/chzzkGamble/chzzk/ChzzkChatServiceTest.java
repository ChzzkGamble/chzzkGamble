package com.chzzkGamble.chzzk;

import com.chzzkGamble.chzzk.chat.service.ChzzkChatService;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.chzzkGamble.gamble.roulette.domain.Roulette;
import com.chzzkGamble.gamble.roulette.service.RouletteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChzzkChatServiceTest {

    private static final String CHANNEL_ID = "0dad8baf12a436f722faa8e5001c5011";
    private static final String CHANNEL_NAME = "따효니";

    private Roulette roulette;

    @Autowired
    ChzzkChatService chzzkChatService;

    @Autowired
    RouletteService rouletteService;

    @BeforeEach
    void setUp() {
        roulette = rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME);
    }

    @Test
    @DisplayName("채팅방과 WebSocket 방식으로 연결할 수 있다.")
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

    @Test
    @DisplayName("WebSocket으로 여러 룰렛과 동시에 연결할 수 있다.")
    void connectChatRoom_concurrency() throws InterruptedException {
        // given
        Roulette roulette1 = rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME);
        Roulette roulette2 = rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME);
        Roulette roulette3 = rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME);

        // when
        chzzkChatService.connectChatRoom(CHANNEL_ID, roulette1.getId());
        chzzkChatService.connectChatRoom(CHANNEL_ID, roulette2.getId());
        chzzkChatService.connectChatRoom(CHANNEL_ID, roulette3.getId());
        Thread.sleep(5000L);

        // then
        assertAll(() -> {
                    assertThat(chzzkChatService.isConnected(roulette1.getId())).isTrue();
                    assertThat(chzzkChatService.isConnected(roulette2.getId())).isTrue();
                    assertThat(chzzkChatService.isConnected(roulette3.getId())).isTrue();
                    assertThat(chzzkChatService.isConnected(roulette.getId())).isFalse();
                });
    }
}
