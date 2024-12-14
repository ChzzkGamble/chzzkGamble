package com.chzzkGamble.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class EmojiParserTest {

    @ParameterizedTest
    @MethodSource("emojiTestCase")
    @DisplayName("문자열에서 이모지를 제거할 수 있다.")
    void encodeWithoutEmoji_removeEmoji(String original, String expected) {
        assertThat(EmojiParser.removeEmojis(original)).isEqualTo(expected);
    }

    static Stream<Arguments> emojiTestCase() {
        return Stream.of(
                Arguments.of("매지컬★슬레이어", "매지컬슬레이어"),
                Arguments.of("사탄맛 캐롤😈", "사탄맛 캐롤"),
                Arguments.of("Hello 🌍! How are you? 😀🚗⚡", "Hello ! How are you? ")
        );
    }
}
