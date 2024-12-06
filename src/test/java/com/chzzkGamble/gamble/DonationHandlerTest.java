package com.chzzkGamble.gamble;

import static org.assertj.core.api.Assertions.assertThat;

import com.chzzkGamble.event.DonationEvent;
import com.chzzkGamble.support.StubDonationEvent;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DonationHandlerTest {

    @Autowired
    private DonationHandler donationHandler;

    @Test
    @DisplayName("룰렛용 도네가 아니라면 투표가 진행되지 않는다.")
    void isNotDonationForVote() throws ExecutionException, InterruptedException {
        DonationEvent donationEvent = StubDonationEvent.ofChat("CN_NAME", "마라탕맛없음", 1_000);

        boolean isVoted = donationHandler.voteGamble(donationEvent).get();

        assertThat(isVoted).isFalse();
    }

    @Test
    @DisplayName("룰렛용 도네라면 투표가 진행된다.")
    void isDonationForVote() throws ExecutionException, InterruptedException {
        DonationEvent donationEvent = StubDonationEvent.ofChat("CN_NAME", "<마라탕>", 1_000);

        boolean isVoted = donationHandler.voteGamble(donationEvent).get();

        assertThat(isVoted).isTrue();
    }
}
