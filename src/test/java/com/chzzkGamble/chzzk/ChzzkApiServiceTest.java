package com.chzzkGamble.chzzk;

import com.chzzkGamble.chzzk.service.ChzzkApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChzzkApiServiceTest {

    @Autowired
    ChzzkApiService chzzkApiService;

    @Test
    @DisplayName("채널 이름을 가져올 수 있다.")
    void getChannelName() {
        String channelName = chzzkApiService.getChannelInfo("0dad8baf12a436f722faa8e5001c5011").getChannelName();
        assertThat(channelName).isEqualTo("따효니");
    }

    @Test
    @DisplayName("채널 이름을 가져올 수 있다2")
    void getChannelName2() {
        String channelName = chzzkApiService.getChannelInfo("0dad8baf12a436f722faa8e5001c50117").getChannelName();
        assertThat(channelName).isEqualTo("(알 수 없음)");
    }
}
