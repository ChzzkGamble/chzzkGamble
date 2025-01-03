package com.chzzkGamble.videodonation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

import com.chzzkGamble.chzzk.chat.domain.Chat;
import com.chzzkGamble.chzzk.chat.repository.ChatRepository;
import com.chzzkGamble.videodonation.domain.VideoDonation;
import com.chzzkGamble.videodonation.dto.Criteria;
import com.chzzkGamble.videodonation.dto.VideoDonationRankingResponses;
import com.chzzkGamble.videodonation.repository.VideoDonationRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class VideoDonationServiceTest {

    private static final String CHANNEL_NAME = "ch_name";
    private static final Clock FIXED = Clock.fixed(Instant.parse("2025-01-02T00:00:00Z"), ZoneId.of("UTC+0"));

    @Autowired
    private VideoDonationService videoDonationService;

    @Autowired
    private VideoDonationRepository videoDonationRepository;

    @Autowired
    private ChatRepository chatRepository;

    @SpyBean
    private Clock clock;

    @DisplayName("현재 방송의 영도 리스트를 조회한다.")
    @Test
    void getRecentlyVideoDonation() {
        Chat chat = new Chat(CHANNEL_NAME);
        chat.open();
        chatRepository.save(chat);
        videoDonationRepository.save(new VideoDonation(CHANNEL_NAME, 1000, "vid-1", "1번영상"));
        videoDonationRepository.save(new VideoDonation(CHANNEL_NAME, 1000, "vid-2", "2번영상"));

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

    @Test
    @DisplayName("기준에 따라 랭킹 목록을 가져올 수 있다 : 치즈")
    @Sql("classpath:videoDonation.sql")
    void getRankingByCriteria_cheese() {
        doReturn(Instant.now(FIXED))
                .when(clock)
                .instant();
        doReturn(ZoneId.of("UTC"))
                .when(clock)
                .getZone();

        VideoDonationRankingResponses rankingByCheese = videoDonationService.getRankingByCriteria(Criteria.CHEESE);

        assertAll(() -> {
                assertThat(rankingByCheese.getRanking().get(0).getVideoId()).isEqualTo("6");
                assertThat(rankingByCheese.getRanking().get(1).getVideoId()).isEqualTo("2");
                assertThat(rankingByCheese.getRanking().get(2).getVideoId()).isEqualTo("1");
                assertThat(rankingByCheese.getRanking().get(3).getVideoId()).isEqualTo("5");
                assertThat(rankingByCheese.getRanking().get(4).getVideoId()).isEqualTo("3");
                assertThat(rankingByCheese.getRanking().size()).isEqualTo(5); // 4번 영상 제외
            }
        );
    }

    @Test
    @DisplayName("기준에 따라 랭킹 목록을 가져올 수 있다 : 개수")
    @Sql("classpath:videoDonation.sql")
    void getRankingByCriteria_count() {
        doReturn(Instant.now(FIXED))
                .when(clock)
                .instant();
        doReturn(ZoneId.of("UTC"))
                .when(clock)
                .getZone();

        VideoDonationRankingResponses rankingByCount = videoDonationService.getRankingByCriteria(Criteria.COUNT);

        assertAll(() -> {
                    assertThat(rankingByCount.getRanking().get(0).getVideoId()).isEqualTo("1");
                    assertThat(rankingByCount.getRanking().get(1).getVideoId()).isEqualTo("2");
                    assertThat(rankingByCount.getRanking().get(2).getVideoId()).isEqualTo("6");
                    assertThat(rankingByCount.getRanking().get(3).getVideoId()).isEqualTo("3");
                    assertThat(rankingByCount.getRanking().get(4).getVideoId()).isEqualTo("5");
                }
        );
    }

    @Test
    @DisplayName("기준에 따라 랭킹 목록을 가져올 수 있다 : 종합")
    @Sql("classpath:videoDonation.sql")
    void getRankingByCriteria_combined() {
        doReturn(Instant.now(FIXED))
                .when(clock)
                .instant();
        doReturn(ZoneId.of("UTC"))
                .when(clock)
                .getZone();

        VideoDonationRankingResponses rankingByCombined = videoDonationService.getRankingByCriteria(Criteria.COMBINED);

        assertAll(() -> {
                    assertThat(rankingByCombined.getRanking().get(0).getVideoId()).isEqualTo("1");
                    assertThat(rankingByCombined.getRanking().get(1).getVideoId()).isEqualTo("2");
                    assertThat(rankingByCombined.getRanking().get(2).getVideoId()).isEqualTo("6");
                    assertThat(rankingByCombined.getRanking().get(3).getVideoId()).isEqualTo("3");
                    assertThat(rankingByCombined.getRanking().get(4).getVideoId()).isEqualTo("5");
                }
        );
    }
}
