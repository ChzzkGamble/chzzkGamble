package com.chzzkGamble.videodonation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.chzzkGamble.chzzk.chat.domain.Chat;
import com.chzzkGamble.chzzk.chat.repository.ChatRepository;
import com.chzzkGamble.videodonation.domain.VideoDonation;
import com.chzzkGamble.videodonation.repository.VideoDonationRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class VideoDonationServiceTest {

    private static final String CHANNEL_NAME = "ch_name";

    @Autowired
    private VideoDonationService videoDonationService;
    @Autowired
    private VideoDonationRepository videoDonationRepository;
    @Autowired
    private ChatRepository chatRepository;

    @DisplayName("현재 방송의 영도 리스트를 조회한다.")
    @Test
    void getRecentlyVideoDonation() {
        Chat chat = new Chat(CHANNEL_NAME);
        chat.open();
        chatRepository.save(chat);
        videoDonationRepository.save(new VideoDonation(CHANNEL_NAME, 1000, "1번영상"));
        videoDonationRepository.save(new VideoDonation(CHANNEL_NAME, 1000, "2번영상"));

        //when
        List<VideoDonation> actual = videoDonationService.getRecentlyVideoDonation(CHANNEL_NAME);

        //then
        assertThat(actual.size()).isEqualTo(2);
    }

    @DisplayName("현재 방송에서 영도가 없다면 빈 리스트를 응답한다.")
    @Test
    void getRecentlyVideoDonation_Empty() {
        //given
        Chat chat = new Chat(CHANNEL_NAME);
        chat.open();
        chatRepository.save(chat);
        chatRepository.save(new Chat(CHANNEL_NAME));

        //when
        List<VideoDonation> recentlyVideoDonation = videoDonationService.getRecentlyVideoDonation(CHANNEL_NAME);

        //then
        assertThat(recentlyVideoDonation).isEmpty();
    }

    @Test
    @DisplayName("연결된 채팅이 없다면, 예외가 발생한다.")
    void isNotExistChat() {
        assertThatThrownBy(() -> videoDonationService.getRecentlyVideoDonation(CHANNEL_NAME))
                .isInstanceOf(IllegalStateException.class);
    }
}
