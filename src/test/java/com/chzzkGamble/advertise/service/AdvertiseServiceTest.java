package com.chzzkGamble.advertise.service;

import com.chzzkGamble.advertise.domain.Advertise;
import com.chzzkGamble.advertise.repository.AdvertiseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AdvertiseServiceTest {

    private static final Clock after10Days;
    static {
        long epochSecond = Clock.system(ZoneId.of("Asia/Seoul")).instant().getEpochSecond();
        epochSecond += 10 * 24 * 60 * 60L + 100; // 10 days + error bound
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
        Advertise advertise = new Advertise("따효니", "image1", 1000L, LocalDateTime.now());
        advertise.approval();
        advertiseRepository.save(advertise);

        // when
        advertiseService.updateAdvertiseMap();

        // then
        assertThat(advertiseService.getAdvertise().getName()).isEqualTo("따효니");
    }

    @Test
    @DisplayName("10일이 지난 광고는 업데이트되지 않는다.")
    void updateAdvertiseMap_after10Days() {
        // given
        Advertise advertise = new Advertise("따효니", "image1", 1000L, LocalDateTime.now());
        advertise.approval();
        advertiseRepository.save(advertise);
        doReturn(Instant.now(after10Days))
                .when(clock)
                .instant();

        // when
        advertiseService.updateAdvertiseMap();

        // then
        assertThat(advertiseService.getAdvertise().getName()).isEqualTo("Default Advertise");
    }

    @Test
    @DisplayName("일정 주기로 광고 목록이 업데이트 된다.") // 3 seconds in test profile
    void updateAdvertiseMap_scheduled() throws InterruptedException {
        // given
        assertThat(advertiseService.getAdvertiseProbabilities()).isEmpty();

        // when
        Advertise advertise = new Advertise("따효니", "image1", 1000L, LocalDateTime.now());
        advertise.approval();
        advertiseRepository.save(advertise);
        Thread.sleep(4 * 1000L);

        // then
        assertThat(advertiseService.getAdvertiseProbabilities()).hasSize(1);
    }

    @Test
    @DisplayName("승인된 광고들만 AdvertiseMap에 저장된다.")
    void approvalAdvertise() {
        // given
        Advertise advertise1 = new Advertise("따효니1", "image1", 1000L, LocalDateTime.now());
        advertise1.approval();
        advertiseRepository.save(advertise1);
        Advertise advertise2 = new Advertise("따효니2", "image2", 1000L, LocalDateTime.now());
        advertiseRepository.save(advertise2);

        // when
        advertiseService.updateAdvertiseMap();

        // then
        assertThat(advertiseService.getAdvertiseProbabilities()).hasSize(1);
    }
}
