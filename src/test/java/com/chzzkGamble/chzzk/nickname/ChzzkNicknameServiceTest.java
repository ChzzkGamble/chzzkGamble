package com.chzzkGamble.chzzk.nickname;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.nickname.service.ChzzkNicknameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChzzkNicknameServiceTest {

    @Autowired
    private ChzzkNicknameService chzzkNicknameService;
    @Autowired
    private ChzzkApiService chzzkApiService;

    @ParameterizedTest
    @MethodSource("provideNicknameData")
    @DisplayName("닉네임으로부터 채널명을 가져올 수 있다.")
    void getChannelNameFromNickname(String nickname, String channelName) {
        assertThat(chzzkNicknameService.getChannelNameFromNickname(nickname))
                .isEqualTo(channelName);
    }

    private static Stream<Arguments> provideNicknameData() {
        return Stream.of(
                Arguments.of("대상현", "따효니"),
                Arguments.of("명훈", "명예훈장")
        );
    }

    @Test
    @DisplayName("닉네임 데이터가 없는 경우 그대로 반환한다.")
    void getChannelNameFromNickname_notExist() {
        String expectedNickname = "ewnfoiwno29hfein";
        assertThat(chzzkNicknameService.getChannelNameFromNickname(expectedNickname))
                .isEqualTo(expectedNickname);
    }

    // 채널명을 올바르게 작성했는지 확인하는 테스트 입니다.
    // 일일이 확인하기 귀찮을 경우 사용할 수 있습니다.
//    @Test
//    @DisplayName("채널명이 올바르게 작성됐는지 확인한다.")
//    void checkChannelNames() {
//        boolean wrongChannelExist = chzzkNicknameService.getNicknames().values().stream()
//                .map(ChannelName::value)
//                .anyMatch(channelName -> {
//                    String apiChannelName = chzzkApiService.getChannelInfo(channelName).getChannelName();
//                    if (apiChannelName.equals(channelName)) {
//                        return false;
//                    }
//                    System.out.println("ChannelName from file : " + channelName);
//                    System.out.println("ChannelName from api : " + apiChannelName);
//                    return true;
//                });
//        assertThat(wrongChannelExist).isFalse();
//    }
}
