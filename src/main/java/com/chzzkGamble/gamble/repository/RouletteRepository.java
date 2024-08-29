package com.chzzkGamble.gamble.repository;

import java.util.UUID;
import com.chzzkGamble.gamble.domain.Roulette;
import org.springframework.data.repository.CrudRepository;

public interface RouletteRepository extends CrudRepository<Roulette, UUID> {
}
