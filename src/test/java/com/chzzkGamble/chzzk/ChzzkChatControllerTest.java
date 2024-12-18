package com.chzzkGamble.chzzk;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.chat.controller.ChzzkChatController;
import com.chzzkGamble.chzzk.chat.dto.ChatConnectRequest;
import com.chzzkGamble.chzzk.chat.service.ChzzkChatService;
import com.chzzkGamble.chzzk.dto.ChannelInfoApiResponse;
import com.chzzkGamble.exception.ChzzkException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChzzkChatControllerTest {

    @Autowired
    ChzzkChatController chzzkChatController;

    @MockBean
    ChzzkApiService chzzkApiService;

    @MockBean
    ChzzkChatService chzzkChatService;

    @Test
    @DisplayName("채널명이 일치하지 않는 경우 연결할 수 없다.")
    void connect_noMatch_channelName() {
        ChannelInfoApiResponse response = new ChannelInfoApiResponse("channelId", "명훈", "url", true, true);

        when(chzzkApiService.getChannelInfo(any())).thenReturn(response);
        doNothing().when(chzzkChatService).connectChatRoom(any());

        assertThatThrownBy(() -> chzzkChatController.connect(new ChatConnectRequest("명예훈장")))
                .isInstanceOf(ChzzkException.class);
    }

    @Test
    @DisplayName("라이브 중이 아닌 경우 연결할 수 없다.")
    void connect_notOnAir() {
        ChannelInfoApiResponse response = new ChannelInfoApiResponse("channelId", "명예훈장", "url", true, false);

        when(chzzkApiService.getChannelInfo("명예훈장")).thenReturn(response);
        doNothing().when(chzzkChatService).connectChatRoom(any());

        assertThatThrownBy(() -> chzzkChatController.connect(new ChatConnectRequest("명예훈장")))
                .isInstanceOf(ChzzkException.class);
    }
}
