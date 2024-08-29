package com.chzzkGamble.gamble.repository;

import java.util.List;
import java.util.UUID;
import com.chzzkGamble.gamble.domain.RouletteElement;
import org.springframework.data.repository.CrudRepository;

public interface RouletteElementRepository extends CrudRepository<RouletteElement, Long> {

    List<RouletteElement> findByRouletteId(UUID id);
}
