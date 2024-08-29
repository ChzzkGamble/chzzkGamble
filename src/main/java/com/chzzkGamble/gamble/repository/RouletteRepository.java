package com.chzzkGamble.gamble.repository;

import java.util.List;
import java.util.UUID;
import com.chzzkGamble.gamble.domain.Roulette;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouletteRepository extends JpaRepository<Roulette, UUID> {

    List<Roulette> findByChannelId(String channelId);
}
