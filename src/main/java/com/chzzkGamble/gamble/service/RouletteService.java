package com.chzzkGamble.gamble.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.chzzkGamble.exception.GambleException;
import com.chzzkGamble.exception.GambleExceptionCode;
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

    private static final int CHEESE_UNIT = 1_000;
    public static final long HOUR_LIMIT = 2L;

    private final RouletteRepository rouletteRepository;
    private final RouletteElementRepository rouletteElementRepository;

    @Transactional
    public Roulette createRoulette(String channelId, String channelName) {
        Roulette roulette = new Roulette(channelId, channelName);
        return rouletteRepository.save(roulette);
    }

    @Transactional(readOnly = true)
    public Roulette readRoulette(UUID rouletteId) {
        return rouletteRepository.findById(rouletteId)
                .orElseThrow(() -> new GambleException(GambleExceptionCode.ROULETTE_NOT_FOUND, "rouletteId : " + rouletteId));
    }

    @Transactional
    public RouletteElement addElement(UUID rouletteId, String elementName) {
        Roulette roulette = readRoulette(rouletteId);
        RouletteElement element = new RouletteElement(elementName, 0, roulette);
        return rouletteElementRepository.save(element);
    }

    @Transactional
    public void addElements(UUID rouletteId, List<String> elementNames) {
        Roulette roulette = readRoulette(rouletteId);
        List<RouletteElement> elements = elementNames.stream()
                .map(name -> new RouletteElement(name, 0, roulette))
                .toList();
        rouletteElementRepository.saveAll(elements);
    }

    @Transactional(readOnly = true)
    public List<RouletteElement> readRouletteElements(UUID rouletteId) {
        return rouletteElementRepository.findByRouletteId(rouletteId);
    }

    @Transactional
    public void vote(String channelId, String msg, int cheese) {
        List<Roulette> roulettes = rouletteRepository.findByChannelIdAndCreatedAtIsAfter(channelId, LocalDateTime.now().minusHours(HOUR_LIMIT));
        roulettes.forEach(roulette -> vote(roulette, msg, cheese));
    }

    @Transactional
    public void vote(Roulette roulette, String msg, int cheese) {
        List<RouletteElement> elements = rouletteElementRepository.findByRouletteId(roulette.getId());
        elements.stream()
                .filter(element -> msg.contains(element.getName()))
                .findFirst()
                .ifPresent(element -> vote(element.getId(), cheese / CHEESE_UNIT));
    }

    @Transactional
    public void vote(Long elementId, int voteCount) {
        RouletteElement element = rouletteElementRepository.findById(elementId)
                .orElseThrow(() -> new GambleException(GambleExceptionCode.ROULETTE_ELEMENT_NOT_FOUND, "elementId : " + elementId));
        element.increaseCount(voteCount);
        rouletteElementRepository.save(element);
    }
}
