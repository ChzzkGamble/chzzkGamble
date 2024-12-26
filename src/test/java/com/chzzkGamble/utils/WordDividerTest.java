package com.chzzkGamble.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WordDividerTest {

    @Test
    @DisplayName("스페이스를 기준으로 둘로 나눈다.")
    void divideSpace() {
        // given
        String s = "abc def";

        // when
        String[] strings = WordDivider.divideSpace(s, 0);

        // then
        assertThat(strings[0]).isEqualTo("abc");
        assertThat(strings[1]).isEqualTo(" def");
    }

    @Test
    @DisplayName("스페이스가 맨 앞에 있을 경우")
    void divideSpace_spaceFront() {
        // given
        String s = " abcdef";

        // when
        String[] strings = WordDivider.divideSpace(s, 0);

        // then
        assertThat(strings[0]).isEqualTo("");
        assertThat(strings[1]).isEqualTo(" abcdef");
    }

    @Test
    @DisplayName("스페이스가 맨 뒤에 있을 경우")
    void divideSpace_spaceBack() {
        // given
        String s = "abcdef ";

        // when
        String[] strings = WordDivider.divideSpace(s, 0);

        // then
        assertThat(strings[0]).isEqualTo("abcdef");
        assertThat(strings[1]).isEqualTo(" ");
    }

    @Test
    @DisplayName("스페이스가 없을 경우")
    void divideSpace_noSpace() {
        // given
        String s = "abcdef";

        // when
        String[] strings = WordDivider.divideSpace(s, 0);

        // then
        assertThat(strings[0]).isEqualTo("abcdef");
        assertThat(strings[1]).isEqualTo("");
    }

    @Test
    @DisplayName("startIndex가 글자 길이를 넘울 경우")
    void divideSpace_overLength() {
        // given
        String s = "abcdef";

        // when
        String[] strings = WordDivider.divideSpace(s, 100);

        // then
        assertThat(strings[0]).isEqualTo("abcdef");
        assertThat(strings[1]).isEqualTo("");
    }

    @Test
    @DisplayName("startIndex 위치에서 가장 가까운 스페이스를 기준으로 나눈다.")
    void divideSpace_startIndex() {
        // given
        String s = "012 456 89";

        // when1
        String[] strings1 = WordDivider.divideSpace(s, 3);

        // then1
        assertThat(strings1[0]).isEqualTo("012");
        assertThat(strings1[1]).isEqualTo(" 456 89");

        // when2
        String[] strings2 = WordDivider.divideSpace(s, 6);

        // then2
        assertThat(strings2[0]).isEqualTo("012 456");
        assertThat(strings2[1]).isEqualTo(" 89");
    }
}