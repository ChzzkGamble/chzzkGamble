package com.chzzkGamble.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AsciiEncoderTest {

    @ParameterizedTest
    @MethodSource("testCase")
    @DisplayName("ascii code에 맞게 인코딩한다.")
    void encode(String original, String encoded) {
        assertThat(AsciiEncoder.encode(original, 150)).isEqualTo(encoded);
    }

    static Stream<Arguments> testCase() {
        return Stream.of(
                Arguments.of("[LIVE] 송밤 / 작두 - 딥플로우 (Feat, 넉살, 허클베리피)", "[LIVE]%20송밤%20%2F%20작두%20%2D%20딥플로우%20%28Feat%2C%20넉살%2C%20허클베리피%29"),
                Arguments.of("[REMAKE]  하디아 - 망창가", "[REMAKE]%20%20하디아%20%2D%20망창가")
        );
    }

    @Test
    @DisplayName("인코딩 할 길이를 미리 지정할 수 있다.")
    void encode_limit() {
        String original = "0123456789";
        String encoded = AsciiEncoder.encode(original, 5);

        assertThat(encoded).isEqualTo("01234");
    }
}
