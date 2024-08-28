package com.chzzkGamble.chzzk;

import com.chzzkGamble.chzzk.service.api.ChzzkApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChzzkApiServiceTest {

    private final String DdahyoniChannelId = "0dad8baf12a436f722faa8e5001c5011";

    @Autowired
    ChzzkApiService chzzkApiService;

    @Test
    @DisplayName("채널 정보를 가져올 수 있다.")
    void getChannelInfo() {
        String channelName = chzzkApiService.getChannelInfo(DdahyoniChannelId).getChannelName();
        assertThat(channelName).isEqualTo("따효니");
    }

    @Test
    @DisplayName("채널 정보를 가져올 수 있다: ChannelId가 잘못된 경우")
    void getChannelInfo_InvalidChannelId() {
        String channelName = chzzkApiService.getChannelInfo(DdahyoniChannelId + "777").getChannelName();
        assertThat(channelName).isEqualTo("(알 수 없음)");
    }

    // 채팅방 id는 변경될 수 있으며, 상황에 따라 실패할 수 있는 테스트 입니다.
    // @ignore 가 필요하다면 붙이기 바랍니다.
    @Test
    @DisplayName("채팅 정보를 가져올 수 있다.")
    void getChatInfo() {
        String chatChannelId = chzzkApiService.getChatInfo(DdahyoniChannelId).getChatChannelId();
        assertThat(chatChannelId).isEqualTo("N1NIoQ");
    }

    @Test
    @DisplayName("채팅 정보를 가져올 수 없다.")
    void getChatInfo_InvalidChannelId_Exception() {
        assertThatThrownBy(() -> chzzkApiService.getChatInfo(DdahyoniChannelId + "777"))
                .isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    @DisplayName("채팅방과 연결할 수 있다.")
    void connectChatRoom() {
    }
}
