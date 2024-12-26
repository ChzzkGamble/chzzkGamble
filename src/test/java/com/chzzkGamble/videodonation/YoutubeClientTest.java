package com.chzzkGamble.videodonation;

import com.chzzkGamble.event.DonationEvent;
import com.chzzkGamble.support.StubDonationEvent;
import com.chzzkGamble.videodonation.youtube.YoutubeClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class YoutubeClientTest {

    @Autowired
    private YoutubeClient client;

    @Disabled("실제 유튜브 서버로 API를 쏘는거라 필요할 때만 키는걸로.. "
            + "진짜 api 요청이 잘가고, 데이터 잘 뽑아오는지 보고싶을 때 쓰면 될 것 같습니다.")
    @ParameterizedTest
    @MethodSource("titleAndVideoId")
    @DisplayName("제목을 통해 영상 id을 추출한다.")
    void getVideoIdByTitleOrNull(String title, String videoId) {
        //when
        String actual = client.getVideoIdByTitleOrNull(title);

        //then
        assertThat(actual).isEqualTo(videoId);
    }

    private static Stream<Arguments> titleAndVideoId() {
        return Stream.of(
                Arguments.of("[10분 테코톡] 우주의 낙관적인 락, 비관적인 락", "era8W7q3CeQ"),
                Arguments.of("[1080P 60FPS] 【まいたけダンス】時間が巻いたときや巻きたいときにご利用ください【儒烏風亭らでん】maitake", "82CBG8QeWnY"),
                Arguments.of("aespa 에스파 'Whiplash' MV", "jWQx2f-CErU"),
                Arguments.of("Kid perfectly recreates Bully Maguire dance #spiderman #tobeymaguire #shorts", "t6CKTjM9dGg"),
                Arguments.of("Deadpool Dance Bye Bye Bye / Step by Step / NSYNC #deadpooldance #viraldancevideo #deadpool #nsync", "ByU-UGtg3Jc"),
                Arguments.of("[LIVE] 송밤 / 작두 - 딥플로우 (Feat, 넉살, 허클베리피)", "X674Fy_TLew")
        );
    }

    @Disabled("실제 Youtube api를 날리는 요청이므로 할당량 소모에 주의할 것")
    @Test
    @DisplayName("제목과 일치하는 영상을 찾지 못하면 null을 반환한다.")
    void getVideoIdByTitleOrNull_noResult() {
        //when
        String actual = client.getVideoIdByTitleOrNull("ejlvfbvbfwkvbfewovbfewcbfwoecbowebocweubfouebvoB");

        //then
        assertThat(actual).isNull();
    }

    @Autowired
    ApplicationEventPublisher publisher;

    @Disabled("실제 Youtube api를 날리는 요청이므로 할당량 소모에 주의할 것")
    @ParameterizedTest
    @MethodSource("youtubeTitles")
    @DisplayName("유튜브 API로 찾지 못한 영상을 확인해보는 테스트입니다.")
    void notFoundInYoutube(String title) throws InterruptedException {
        DonationEvent event = StubDonationEvent.ofVideo("channelName", title, 1000);
        publisher.publishEvent(event);

        Thread.sleep(5000L);
    }

    private static Stream<Arguments> youtubeTitles() {
        return Stream.of(
                Arguments.of("[메이플스토리M] 엔젤릭버스터 「Star Bubble」 Official Lyric Video")
        );
    }
}
