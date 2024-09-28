package com.chzzkGamble.chzzk;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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
    @DisplayName("채널 정보를 가져올 수 없다: ChannelId가 잘못된 경우")
    void getChannelInfo_InvalidChannelId() {
        assertThatThrownBy(() -> chzzkApiService.getChannelInfo(DdahyoniChannelId + "777"))
                .isInstanceOf(ChzzkException.class)
                .hasMessage(ChzzkExceptionCode.CHANNEL_ID_INVALID.getMessage());
    }

    @Test
    @DisplayName("채팅 정보를 가져올 수 있다.")
    void getChatInfo() {
        assertThatCode(() -> chzzkApiService.getChatInfo(DdahyoniChannelId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("채팅 정보를 가져올 수 없다.: ChannelId가 잘못된 경우")
    void getChatInfo_InvalidChannelId_Exception() {
        assertThatThrownBy(() -> chzzkApiService.getChatInfo(DdahyoniChannelId + "777"))
                .isInstanceOf(HttpClientErrorException.NotFound.class);
    }
}
