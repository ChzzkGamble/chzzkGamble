package com.chzzkGamble.gamble.service;

import java.util.List;
import java.util.UUID;
import com.chzzkGamble.gamble.domain.Roulette;
import com.chzzkGamble.gamble.domain.RouletteElement;
import com.chzzkGamble.gamble.repository.RouletteElementRepository;
import com.chzzkGamble.gamble.repository.RouletteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RouletteService {

    private final RouletteRepository rouletteRepository;
    private final RouletteElementRepository rouletteElementRepository;

    @Transactional
    public Roulette createRoulette(String channelId) {
        UUID id = UUID.randomUUID();
        Roulette roulette = new Roulette(id, channelId);
        return rouletteRepository.save(roulette);
    }

    @Transactional(readOnly = true)
    public Roulette readRoulette(UUID rouletteId) {
        return rouletteRepository.findById(rouletteId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 룰렛입니다."));
    }

    @Transactional
    public RouletteElement addElement(UUID rouletteId, String elementName) {
        Roulette roulette = readRoulette(rouletteId);
        RouletteElement element = new RouletteElement(elementName, 0, roulette);
        return rouletteElementRepository.save(element);
    }

    @Transactional(readOnly = true)
    public List<RouletteElement> readRouletteElements(UUID rouletteId) {
        return rouletteElementRepository.findByRouletteId(rouletteId);
    }

    @Transactional
    public void vote(Long elementId, int voteCount) {
        RouletteElement element = rouletteElementRepository.findById(elementId)
                .orElseThrow(() -> new RuntimeException("요소를 찾을 수 없습니다. " + elementId));
        element.increaseCount(voteCount);
    }
}
