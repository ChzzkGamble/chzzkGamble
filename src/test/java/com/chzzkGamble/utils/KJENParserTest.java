package com.chzzkGamble.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class KJENParserTest {

    @ParameterizedTest
    @MethodSource("emojiTestCase")
    @DisplayName("문자열에서 이모지를 제거할 수 있다.")
    void encodeWithoutEmoji_removeEmoji(String original, String expected) {
        assertThat(KJENParser.extractKJEN(original)).isEqualTo(expected);
    }

    static Stream<Arguments> emojiTestCase() {
        return Stream.of(
                Arguments.of("매지컬★슬레이어", "매지컬슬레이어"),
                Arguments.of("사탄맛 캐롤😈", "사탄맛 캐롤"),
                Arguments.of("Hello 🌍! How are you? 😀🚗⚡", "Hello ! How are you? "),
                Arguments.of("-롤 애니메이션- 조이는♂ Boy♥~(원피스 조이는보이 패러디)", "-롤 애니메이션- 조이는 Boy~(원피스 조이는보이 패러디)"),
                Arguments.of("캐롤 군대버전\uD83C\uDF85\uD83D\uDC80\uD83C\uDFB9", "캐롤 군대버전"),
                Arguments.of("\uD83D\uDD25 버니스!! 난 네가 좋다!! 올해 수능 금지곡 여기 하나 추가요!", " 버니스!! 난 네가 좋다!! 올해 수능 금지곡 여기 하나 추가요!"),
                Arguments.of("【Blue Archive】Shiroko ＆ Nonomi 「Worth It」【ブルアカ MMD シロコ ノノミ】", "【Blue Archive】Shiroko ＆ Nonomi 「Worth It」【ブルアカ MMD シロコ ノノミ】"),
                Arguments.of("역사상 가장 어려운 카드 기술 TOP 5 \uD83C\uDCCF feat. 다니엘 로이", "역사상 가장 어려운 카드 기술 TOP 5  feat. 다니엘 로이"),
                Arguments.of("🏴‍☠️振付師がパイパイ仮面でどうかしらん？ 踊ってみた #宝鐘マリン #hololive #ホロライブ #shorts", "振付師がパイパイ仮面でどうかしらん？ 踊ってみた #宝鐘マリン #hololive #ホロライブ #shorts")
        );
    }
}
