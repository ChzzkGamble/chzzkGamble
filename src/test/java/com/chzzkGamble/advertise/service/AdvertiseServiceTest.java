package com.chzzkGamble.advertise.service;

import com.chzzkGamble.advertise.domain.Advertise;
import com.chzzkGamble.advertise.repository.AdvertiseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdvertiseServiceTest {

    private static final Clock after10Days;
    static {
        long epochSecond = Clock.system(ZoneId.of("Asia/Seoul")).instant().getEpochSecond();
        epochSecond += 10 * 24 * 60 * 60L + 10; // 10 days + error bound
        after10Days = Clock.fixed(Instant.ofEpochSecond(epochSecond), ZoneId.of("Asia/Seoul"));
    }

    @Autowired
    AdvertiseRepository advertiseRepository;

    @Autowired
    AdvertiseService advertiseService;

    @SpyBean
    Clock clock;

    @Test
    @DisplayName("광고 목록을 업데이트할 수 있다.")
    void updateAdvertiseMap() {
        // given
        advertiseRepository.save(new Advertise("따효니", "image1", 1000, true));

        // when
        advertiseService.updateAdvertiseMap();

        // then
        assertThat(advertiseService.getAdvertise().getName()).isEqualTo("따효니");
    }

    @Test
    @DisplayName("10일이 지난 광고는 업데이트되지 않는다.")
    void updateAdvertiseMap_after10Days() {
        // given
        advertiseRepository.save(new Advertise("따효니", "image1", 1000, true));
        doReturn(Instant.now(after10Days))
                .when(clock)
                .instant();

        // when
        advertiseService.updateAdvertiseMap();

        // then
        assertThat(advertiseService.getAdvertise().getName()).isEqualTo("Default Advertise");
    }
}
