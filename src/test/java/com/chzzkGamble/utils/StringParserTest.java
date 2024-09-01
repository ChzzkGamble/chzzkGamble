package com.chzzkGamble.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringParserTest {

    @ParameterizedTest
    @MethodSource("provideText")
    @DisplayName("택스트를 파싱할 수 있다.")
    void parseFromTo(String text, String parsedText) {
        // given & when
        String parsed = StringParser.parseFromTo(text, '<', '>');

        // then
        assertThat(parsed).isEqualTo(parsedText);
    }

    private static Stream<Arguments> provideText() {
        return Stream.of(
                Arguments.of("<글>", "글"),
                Arguments.of("<이 글자>", "이 글자"),
                Arguments.of("<abcdef>", "abcdef"),
                Arguments.of("<text_!@#>", "text_!@#")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidText")
    @DisplayName("잘못된 택스트는 파싱할 수 없다.")
    void parseFromTo_invalidText_exception(String text) {
        // given & when & then
        assertThatThrownBy(() -> StringParser.parseFromTo(text, '{', '}'))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideInvalidText() {
        return Stream.of(
                Arguments.of("{}"), // 빈 문자열
                Arguments.of("}글{"), // 순서 반대
                Arguments.of("{글"), // 오른쪽 구분기호 없음
                Arguments.of("글}"), // 왼쪽 구분기호 없음
                Arguments.of("<글자>") // 구분기호 없음
        );
    }

}