package com.chzzkGamble.advertise.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AdvertiseMapTest {

    private static final Advertise ad1 = new Advertise("ad1","url1", 1000, true);
    private static final Advertise ad2 = new Advertise("ad2","url2", 3000, true);
    private static final Advertise ad3 = new Advertise("ad3","url3", 5000, true);
    private static final Advertise ad4 = new Advertise("ad4","url3", 7000, false);

    @Test
    @DisplayName("광고 Map을 만들 수 있다.")
    void from() {
        // given & when & then
        assertThatCode(() -> AdvertiseMap.from(List.of(ad1, ad2, ad3, ad4)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("빈 광고 Map을 만들 수 있다.")
    void from_emptyList() {
        // given & when & then
        assertThatCode(() -> AdvertiseMap.from(Collections.emptyList()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("광고 확률을 정확히 표시할 수 있다.")
    void getProbabilities() {
        // given
        AdvertiseMap advertiseMap = AdvertiseMap.from(List.of(ad1, ad2, ad3, ad4));

        // when
        Map<Advertise, Double> probabilities = advertiseMap.getProbabilities();

        // then
        assertAll(() -> {
            assertThat(Double.compare(probabilities.get(ad1), 1_000/ (double) 9_000)).isEqualTo(0);
            assertThat(Double.compare(probabilities.get(ad2), 3_000/ (double) 9_000)).isEqualTo(0);
            assertThat(Double.compare(probabilities.get(ad3), 5_000/ (double) 9_000)).isEqualTo(0);
        });
    }

    @Test
    @DisplayName("승인되지 않은 광고는 확률 상 존재하지 않는다.")
    void getProbabilities_notApproved() {
        // given
        AdvertiseMap advertiseMap = AdvertiseMap.from(List.of(ad1, ad2, ad3, ad4));

        // when
        Map<Advertise, Double> probabilities = advertiseMap.getProbabilities();

        // then
        assertThat(probabilities.containsKey(ad4)).isFalse();
    }

    @RepeatedTest(100)
    @DisplayName("랜덤 광고를 확률에 맞게 획득할 수 있다.")
    void getRandom() {
        // given
        AdvertiseMap advertiseMap = AdvertiseMap.from(List.of(ad1, ad2, ad3, ad4));

        // when
        int ad1Count = 0;
        int ad2Count = 0;
        int ad3Count = 0;
        int ad4Count = 0;

        for (int i = 0; i < 9000; i++) {
            Advertise ad = advertiseMap.getRandom();
            if (ad.equals(ad1)) ad1Count++;
            if (ad.equals(ad2)) ad2Count++;
            if (ad.equals(ad3)) ad3Count++;
            if (ad.equals(ad4)) ad4Count++;
        }

        // then
        assertThat(ad1Count).isBetween(500, 1500);
        assertThat(ad2Count).isBetween(2500, 3500);
        assertThat(ad3Count).isBetween(4500, 5500);
        assertThat(ad4Count).isEqualTo(0);
    }

    @Test
    @DisplayName("빈 Map일 경우, 기본 광고가 나온다")
    void getRandom_empty() {
        // given
        AdvertiseMap advertiseMap = AdvertiseMap.from(Collections.emptyList());

        // when & then
        assertThat(advertiseMap.getRandom().getName()).isEqualTo("Default Advertise");
    }
}
