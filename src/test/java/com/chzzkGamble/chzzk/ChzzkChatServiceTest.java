package com.chzzkGamble.chzzk;

import com.chzzkGamble.chzzk.chat.service.ChzzkChatService;
import com.chzzkGamble.chzzk.dto.DonationMessage;
import com.chzzkGamble.event.DonationEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChzzkChatServiceTest {

    private static final String CHANNEL_NAME = "따효니";
    private static final WebSocketMessage<?> message = new TextMessage("{\"svcid\":\"game\",\"ver\":\"1\",\"bdy\":[{\"svcid\":\"game\",\"cid\":\"N1Q-4g\",\"mbrCnt\":5548,\"uid\":\"anonymous\",\"profile\":null,\"msg\":\"<차렷자세>에서 손이 따봉으로 바뀔수는 있어도 크게 벗어나는 자세는 인싸다....\",\"msgTypeCode\":10,\"msgStatusType\":\"NORMAL\",\"extras\":\"{\\\"isAnonymous\\\":true,\\\"payType\\\":\\\"CURRENCY\\\",\\\"payAmount\\\":1000,\\\"donationId\\\":\\\"2wQtTN5iMhyeiRXLADF4GyUQ7hfmZ\\\",\\\"donationType\\\":\\\"CHAT\\\",\\\"weeklyRankList\\\":[]}\",\"ctime\":1727940364316,\"utime\":1727940364316,\"msgTid\":null,\"msgTime\":1727940364312}],\"cmd\":93102,\"tid\":null,\"cid\":\"N1Q-4g\"}");
    private static final Clock PAST_CLOCK = Clock.fixed(Instant.parse("2024-08-24T11:00:00Z"), ZoneId.of("Asia/Seoul"));
    private static final Clock FUTURE_CLOCK = Clock.fixed(Instant.parse("2024-08-24T12:20:00Z"), ZoneId.of("Asia/Seoul"));

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    ChzzkChatService chzzkChatService;

    @SpyBean
    Clock clock;

    @Test
    @DisplayName("채팅방과 WebSocket 방식으로 연결할 수 있다.")
    void connectChatRoom() {
        assertThatCode(() -> chzzkChatService.connectChatRoom(CHANNEL_NAME))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("마지막 도네이션으로부터 일정 시간이 지난 채널은 자동으로 연결을 끊는다.")
    void disconnectChatRoom() throws InterruptedException {
        // given
        doReturn(Instant.now(PAST_CLOCK))
                .when(clock)
                .instant();
        chzzkChatService.connectChatRoom(CHANNEL_NAME);

        // when
        doReturn(Instant.now(FUTURE_CLOCK))
                .when(clock)
                .instant();
        Thread.sleep(5000L);

        // then
        assertThat(chzzkChatService.isConnected(CHANNEL_NAME)).isFalse();
    }

    @Test
    @DisplayName("마지막 도네이션으로부터 일정 시간이 지나지 않은 채널은 연결을 유지한다.")
    void keepConnectChatRoom() throws InterruptedException {
        // given
        doReturn(Instant.now(PAST_CLOCK))
                .when(clock)
                .instant();
        chzzkChatService.connectChatRoom(CHANNEL_NAME);

        doReturn(Instant.now(FUTURE_CLOCK))
                .when(clock)
                .instant();
        eventPublisher.publishEvent(new DonationEvent(new DonationMessage(CHANNEL_NAME, message)));

        Thread.sleep(5000L);

        // then
        assertThat(chzzkChatService.isConnected(CHANNEL_NAME)).isTrue();
    }
}
