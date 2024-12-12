package com.chzzkGamble.videodonation.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.chzzkGamble.event.DonationEvent;
import com.chzzkGamble.support.StubDonationEvent;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class VideoDonationHandlerTest {

    @Autowired
    private VideoDonationHandler videoDonationHandler;

    @DisplayName("영상 도네가 아니면 서비스 로직을 진행하지 않는다.")
    @Test
    void not_Handler_Video_Donation() throws ExecutionException, InterruptedException {
        //given
        DonationEvent donationEvent = StubDonationEvent.ofChat("CN_NAME", "마라탕맛없음", 1_000);

        //when
        CompletableFuture<Boolean> actual = videoDonationHandler.handleVideoDonation(donationEvent);

        //then
        assertThat(actual.get()).isFalse();
    }

    @DisplayName("영상 도네가 아니면 서비스 로직을 진행한다.")
    @Test
    void handler_Video_Donation() throws ExecutionException, InterruptedException {
        //given
        DonationEvent donationEvent = StubDonationEvent.ofVideo("CN_NAME", "[10분 테코톡] 우주의 낙관적인 락, 비관적인 락", 1_000);

        //when
        CompletableFuture<Boolean> actual = videoDonationHandler.handleVideoDonation(donationEvent);

        //then
        assertThat(actual.get()).isTrue();
    }
}
