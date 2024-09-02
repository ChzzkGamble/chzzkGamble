package com.chzzkGamble.gamble.roulette.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.chzzkGamble.gamble.roulette.domain.Roulette;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouletteRepository extends JpaRepository<Roulette, UUID> {

    List<Roulette> findByChannelNameAndCreatedAtIsAfter(String channelName, LocalDateTime dateTime);

    Optional<Roulette> findByIdAndCreatedAtAfter(UUID id, LocalDateTime dateTime);
}
