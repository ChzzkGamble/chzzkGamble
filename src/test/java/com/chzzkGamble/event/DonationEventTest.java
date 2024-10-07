package com.chzzkGamble.event;

import com.chzzkGamble.chzzk.dto.DonationMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DonationEventTest {

    private static final WebSocketMessage<?> message = new TextMessage("{\"svcid\":\"game\",\"ver\":\"1\",\"bdy\":[{\"svcid\":\"game\",\"cid\":\"N1Q-4g\",\"mbrCnt\":5548,\"uid\":\"anonymous\",\"profile\":null,\"msg\":\"<차렷자세>에서 손이 따봉으로 바뀔수는 있어도 크게 벗어나는 자세는 인싸다....\",\"msgTypeCode\":10,\"msgStatusType\":\"NORMAL\",\"extras\":\"{\\\"isAnonymous\\\":true,\\\"payType\\\":\\\"CURRENCY\\\",\\\"payAmount\\\":1000,\\\"donationId\\\":\\\"2wQtTN5iMhyeiRXLADF4GyUQ7hfmZ\\\",\\\"donationType\\\":\\\"CHAT\\\",\\\"weeklyRankList\\\":[]}\",\"ctime\":1727940364316,\"utime\":1727940364316,\"msgTid\":null,\"msgTime\":1727940364312}],\"cmd\":93102,\"tid\":null,\"cid\":\"N1Q-4g\"}");

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    TestEventListener eventListener;

    @Test
    @DisplayName("도네이션 이벤트를 발행하면 이벤트 리스너로 확인할 수 있다.")
    void listenEvent() throws InterruptedException {
        // given
        DonationMessage donationMessage = new DonationMessage("channelName", message);

        // when
        publisher.publishEvent(new DonationEvent(donationMessage));

        Thread.sleep(500L);

        // then
        assertThat(eventListener.isListen()).isTrue();
    }

    @TestConfiguration
    static class EventListenerConfig {

        @Bean
        TestEventListener testEventListener() {
            return new TestEventListener();
        }
    }
}
