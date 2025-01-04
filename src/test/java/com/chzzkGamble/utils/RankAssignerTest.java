package com.chzzkGamble.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RankAssignerTest {

    @Test
    @DisplayName("컬렉션의 랭킹을 구할 수 있다.")
    void getRankings() {
        List<String> collection = List.of("3번째", "4번째", "2번째", "1번째");
        List<Ranking<String>> rankings = RankAssigner.assignRanking(collection, e -> Integer.parseInt(e.substring(0, 1)));

        assertAll(() -> {
            assertThat(rankings.get(0).getElement()).isEqualTo("4번째");
            assertThat(rankings.get(0).getRank()).isEqualTo(1);
            assertThat(rankings.get(1).getElement()).isEqualTo("3번째");
            assertThat(rankings.get(1).getRank()).isEqualTo(2);
            assertThat(rankings.get(2).getElement()).isEqualTo("2번째");
            assertThat(rankings.get(2).getRank()).isEqualTo(3);
            assertThat(rankings.get(3).getElement()).isEqualTo("1번째");
            assertThat(rankings.get(3).getRank()).isEqualTo(4);
        });
    }

    @Test
    @DisplayName("값이 같은 경우 rank도 같다.")
    void getRankings_sameValue() {
        List<String> collection = List.of("1번째", "2번째");
        List<Ranking<String>> rankings = RankAssigner.assignRanking(collection, e -> 100);

        assertAll(() -> {
            assertThat(rankings.get(0).getElement()).isEqualTo("1번째");
            assertThat(rankings.get(0).getRank()).isEqualTo(1);
            assertThat(rankings.get(1).getElement()).isEqualTo("2번째");
            assertThat(rankings.get(1).getRank()).isEqualTo(1);
        });
    }

    @Test
    @DisplayName("값이 같은 경우 순서는 기존 컬렉션과 같다.")
    void getRankings_sameValueSameOrder() {
        List<Integer> collection = IntStream.range(1, 101)
                .boxed()
                .collect(Collectors.toList());
        List<Ranking<Integer>> rankings = RankAssigner.assignRanking(collection, e -> 100);

        assertAll(() -> {
            for (int i = 1; i < 101; i++) {
                assertThat(rankings.get(i - 1).getElement()).isEqualTo(i);
            }
        });
    }

}