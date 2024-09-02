package com.chzzkGamble.gamble;

import com.chzzkGamble.gamble.roulette.domain.Roulette;
import com.chzzkGamble.gamble.roulette.repository.RouletteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Sql(value = "classpath:roulette.sql") // insert 12 roulettes at 2024-08-30T00:00:00 for 2 hour interval
public class RouletteRepositoryTest {

    private static final String CHANNEL_NAME = "ch_name";

    @Autowired
    RouletteRepository rouletteRepository;

    @Test
    @DisplayName("특정 시간 이후 룰렛만 가져올 수 있다.")
    void findByChannelNameAndCreatedAtIsAfter() {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 8, 30, 3, 0, 0);

        // when
        List<Roulette> roulettes = rouletteRepository.findByChannelNameAndCreatedAtIsAfter(CHANNEL_NAME, localDateTime);

        // then
        assertThat(roulettes).hasSize(10);
    }
}

