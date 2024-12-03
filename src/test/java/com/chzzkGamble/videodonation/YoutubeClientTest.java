package com.chzzkGamble.videodonation;

import com.chzzkGamble.videodonation.youtube.YoutubeClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class YoutubeClientTest {

    @Autowired
    private YoutubeClient client;

    @Disabled("실제 유튜브 서버로 API를 쏘는거라 필요할 때만 키는걸로.. "
            + "진짜 api 요청이 잘가고, 데이터 잘 뽑아오는지 보고싶을 때 쓰면 될 것 같습니다."
            + "테스트할 때, 프로덕션 코드 youtube 관련 설정 test 쪽에 추가하고 하세요"
            + "실수로라도 커밋하시면 안됩니다. API Key 있어요.")
    @Test
    @DisplayName("제목을 통해 영상 url을 추출한다.")
    void getURLByTitle() {
        //given
        String title = "[10분 테코톡] 우주의 낙관적인 락, 비관적인 락";

        //when
        String actual = client.getURLByTitle(title);

        //then
        Assertions.assertThat(actual).isEqualTo("https://www.youtube.com/watch?v=era8W7q3CeQ");
    }
}
