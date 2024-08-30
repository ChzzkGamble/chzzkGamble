package com.chzzkGamble.gamble;

import com.chzzkGamble.gamble.roulette.domain.Roulette;
import com.chzzkGamble.gamble.roulette.domain.RouletteElement;
import com.chzzkGamble.gamble.roulette.repository.RouletteElementRepository;
import com.chzzkGamble.gamble.roulette.service.RouletteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouletteServiceTest {

    private static final String CHANNEL_ID = "ch_id";
    private static final String CHANNEL_NAME = "ch_name";

    @Autowired
    RouletteService rouletteService;
    @Autowired
    RouletteElementRepository rouletteElementRepository;

    @Test
    @DisplayName("룰렛을 생성할 수 있다.")
    void createRoulette() {
        // given & when & then
        assertThatCode(() -> rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("룰렛을 불러올 수 있다.")
    void readRoulette() {
        // given
        Roulette roulette = rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME);

        // when & then
        assertThatCode(() -> rouletteService.readRoulette(roulette.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 룰렛을 불러올 수 없다.")
    void readRoulette_invalidId_exception() {
        // given & when & then
        assertThatThrownBy(() -> rouletteService.readRoulette(UUID.randomUUID()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("룰렛 요소를 추가할 수 있다.")
    void addElement() {
        // given
        Roulette roulette = rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME);

        // when & then
        assertThatCode(() -> rouletteService.addElement(roulette.getId(), "요소1"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("룰렛 요소 목록을 불러올 수 있다.")
    void readElements() {
        // given
        Roulette roulette = rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME);
        RouletteElement element1 = rouletteService.addElement(roulette.getId(), "요소1");
        RouletteElement element2 = rouletteService.addElement(roulette.getId(), "요소2");

        // when & then
        assertThat(rouletteService.readRouletteElements(roulette.getId()))
                .containsExactlyInAnyOrder(element1, element2);
    }

    @Test
    @DisplayName("룰렛 요소에 투표할 수 있다.")
    void vote() {
        // given
        Roulette roulette = rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME);
        RouletteElement element = rouletteService.addElement(roulette.getId(), "요소");

        // when
        rouletteService.vote(CHANNEL_ID, "요소", 3_000);

        // then
        RouletteElement votedElement = rouletteElementRepository.findById(element.getId()).orElseThrow();
        assertThat(votedElement.getCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("룰렛 요소에 음수 개수만큼 투표할 수 없다.")
    void vote_minusVote_Exception() {
        // given
        Roulette roulette = rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME);
        rouletteService.addElement(roulette.getId(), "요소");

        // when & then
        assertThatThrownBy(() -> rouletteService.vote(CHANNEL_ID, "요소", -1_000))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("룰렛 요소 투표에 동시성 제어가 되어있다.")
    void vote_concurrencyControl() throws InterruptedException {
        // given
        Roulette roulette = rouletteService.createRoulette(CHANNEL_ID, CHANNEL_NAME);
        RouletteElement element = rouletteService.addElement(roulette.getId(), "요소");

        // when
        int threadsCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        for (int i = 0; i < threadsCount; i++) {
            executorService.submit(() ->
                rouletteService.vote(CHANNEL_ID, "요소", 1_000)
            );
        }
        executorService.shutdown();
        Thread.sleep(1000L);

        // then
        RouletteElement votedElement = rouletteElementRepository.findById(element.getId()).orElseThrow();
        assertThat(votedElement.getCount()).isEqualTo(10);
    }
}
