package com.chzzkGamble.advertise.domain;

import com.chzzkGamble.exception.AdvertiseException;
import com.chzzkGamble.exception.AdvertiseExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdvertiseTest {

    private final Clock clock = Clock.system(ZoneId.of("Asia/Seoul"));

    @ParameterizedTest
    @ValueSource(ints = {0, 31})
    @DisplayName("광고 기간은 1 ~ 30일 이내로만 가능하다")
    void invalidAdPeriod(int adPeriod) {
        assertThatThrownBy(() -> new Advertise("name", "imageUrl", 1000L, adPeriod))
                .isInstanceOf(AdvertiseException.class)
                .hasMessage(AdvertiseExceptionCode.INVALID_AD_PERIOD.getMessage());
    }

    @Test
    @DisplayName("이미 승인된 광고를 승인하면 예외를 발생시킨다")
    void repeatApproval() {
        Advertise advertise = new Advertise("name", "imageUrl", 1000L, 10);
        advertise.approval(clock);

        assertThatThrownBy(() -> advertise.approval(clock))
                .isInstanceOf(AdvertiseException.class)
                .hasMessage(AdvertiseExceptionCode.ALREADY_APPROVED_AD.getMessage());
    }

    @Test
    @DisplayName("승인되지 않은 광고를 승인 취소하면 예외를 발생시킨다")
    void repeatRejection() {
        Advertise advertise = new Advertise("name", "imageUrl", 1000L, 10);

        assertThatThrownBy(advertise::rejection)
                .isInstanceOf(AdvertiseException.class)
                .hasMessage(AdvertiseExceptionCode.AD_NOT_APPROVED.getMessage());
    }

    @Test
    @DisplayName("0원 이하의 금액을 입력하면 예외를 발생시킨다")
    void invalidCost() {
        assertThatThrownBy(() -> new Advertise("name", "imageUrl", 0L, 10))
                .isInstanceOf(AdvertiseException.class)
                .hasMessage(AdvertiseExceptionCode.COST_UNDER_ZERO.getMessage());
    }
}
