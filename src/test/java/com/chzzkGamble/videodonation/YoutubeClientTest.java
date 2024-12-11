package com.chzzkGamble.videodonation;

import com.chzzkGamble.videodonation.youtube.YoutubeClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import java.util.stream.Stream;

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
    void getVideoIdByTitle(String title, String videoId) {
        //when
        String actual = client.getVideoIdByTitle(title);

        //then
        Assertions.assertThat(actual).isEqualTo(videoId);
    }

    private static Stream<Arguments> titleAndVideoId() {
        return Stream.of(
                Arguments.of("[10분 테코톡] 우주의 낙관적인 락, 비관적인 락", "era8W7q3CeQ"),
                Arguments.of("[1080P 60FPS] 【まいたけダンス】時間が巻いたときや巻きたいときにご利用ください【儒烏風亭らでん】maitake", "82CBG8QeWnY"),
                Arguments.of("aespa 에스파 'Whiplash' MV", "jWQx2f-CErU"),
                Arguments.of("듣고또듣고 Live-through\uD83C\uDFA7 이문세(Moon-Sae Lee) 옛사랑,깊은밤을날아서, 가로수그늘아래서면,소녀,사랑이 지나가면,붉은노을,광화문연가,휘파람,가을이 오면..", "nic2pkI-eHQ"),
                Arguments.of("Kid perfectly recreates Bully Maguire dance #spiderman #tobeymaguire #shorts", "t6CKTjM9dGg"),
                Arguments.of("Deadpool Dance Bye Bye Bye / Step by Step / NSYNC #deadpooldance #viraldancevideo #deadpool #nsync", "ByU-UGtg3Jc")
        );
    }
}