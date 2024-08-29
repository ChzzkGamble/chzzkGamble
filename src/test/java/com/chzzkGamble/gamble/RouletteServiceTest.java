package com.chzzkGamble.gamble;

import com.chzzkGamble.gamble.domain.Roulette;
import com.chzzkGamble.gamble.domain.RouletteElement;
import com.chzzkGamble.gamble.service.RouletteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouletteServiceTest {

    private static final String CHANNEL_ID = "ch_id";

    @Autowired
    RouletteService rouletteService;

    @Test
    @DisplayName("룰렛을 생성할 수 있다.")
    void createRoulette() {
        // given & when & then
        assertThatCode(() -> rouletteService.createRoulette(CHANNEL_ID))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("룰렛을 불러올 수 있다.")
    void readRoulette() {
        // given
        Roulette roulette = rouletteService.createRoulette(CHANNEL_ID);

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
        Roulette roulette = rouletteService.createRoulette(CHANNEL_ID);

        // when & then
        assertThatCode(() -> rouletteService.addElement(roulette.getId(), "요소1"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("룰렛 요소 목록을 불러올 수 있다.")
    void readElements() {
        // given
        Roulette roulette = rouletteService.createRoulette(CHANNEL_ID);
        RouletteElement element1 = rouletteService.addElement(roulette.getId(), "요소1");
        RouletteElement element2 = rouletteService.addElement(roulette.getId(), "요소2");

        // when & then
        assertThat(rouletteService.readRouletteElements(roulette.getId()))
                .containsExactlyInAnyOrder(element1, element2);
    }
}
