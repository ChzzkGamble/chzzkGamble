package com.chzzkGamble.gamble.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.chzzkGamble.gamble.domain.Roulette;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouletteRepository extends JpaRepository<Roulette, UUID> {

    List<Roulette> findByChannelIdAndCreatedAtIsAfter(String channelId, LocalDateTime dateTime);

    Optional<Roulette> findByIdAndCreatedAtAfter(UUID id, LocalDateTime dateTime);
}
