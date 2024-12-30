package com.chzzkGamble.chzzk.chat.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatTest {

    @Test
    @DisplayName("채팅 객체의 instanceId는 어플리케이션 실행 매체에 의해 결정된다.")
    void instanceId() {
        Chat chat = new Chat("channel");

        assertThat(chat.getInstanceId()).isEqualTo("test");
    }
}
