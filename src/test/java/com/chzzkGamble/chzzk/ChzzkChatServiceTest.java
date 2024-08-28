package com.chzzkGamble.chzzk;

import com.chzzkGamble.chzzk.chat.ChzzkChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChzzkChatServiceTest {

    @Autowired
    ChzzkChatService chzzkChatService;

    @Test
    @DisplayName("채팅방과 WebScoket 방식으로 연결할 수 있다")
    void connectChatRoom() {
        chzzkChatService.connectChatRoom("0dad8baf12a436f722faa8e5001c5011");
    }
}
