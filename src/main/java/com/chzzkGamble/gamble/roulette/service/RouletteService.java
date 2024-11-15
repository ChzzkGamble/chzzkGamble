package com.chzzkGamble.gamble.roulette.service;

import com.chzzkGamble.exception.GambleException;
import com.chzzkGamble.exception.GambleExceptionCode;
import com.chzzkGamble.gamble.roulette.domain.Roulette;
import com.chzzkGamble.gamble.roulette.domain.RouletteElement;
import com.chzzkGamble.gamble.roulette.repository.RouletteElementRepository;
import com.chzzkGamble.gamble.roulette.repository.RouletteRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RouletteService {

    private static final long HOUR_LIMIT = 5L;

    private final RouletteRepository rouletteRepository;
    private final RouletteElementRepository rouletteElementRepository;

    @Transactional
    public Roulette createRoulette(String channelName) {
        Roulette roulette = new Roulette(channelName);
        return rouletteRepository.save(roulette);
    }

    @Transactional(readOnly = true)
    public Roulette readRoulette(UUID rouletteId) {
        return rouletteRepository.findByIdAndCreatedAtAfter(rouletteId, LocalDateTime.now().minusHours(HOUR_LIMIT))
                .orElseThrow(() -> new GambleException(
                        GambleExceptionCode.ROULETTE_NOT_FOUND,
                        "rouletteId : " + rouletteId));
    }

    @Transactional(readOnly = true)
    public List<RouletteElement> readRouletteElements(UUID rouletteId) {
        return rouletteElementRepository.findByRouletteId(rouletteId);
    }

    @Transactional
    public void vote(String channelName, String elementName, int cheese) {
        List<Roulette> roulettes = rouletteRepository.findByChannelNameAndVotingIsTrue(channelName);
        for (Roulette roulette : roulettes) {
            RouletteElement element = rouletteElementRepository.findByNameAndRouletteId(elementName, roulette.getId())
                    .orElse(RouletteElement.newInstance(elementName, roulette));

            element.increaseCount(cheese);
            rouletteElementRepository.save(element);
        }
    }

    @Transactional
    public Roulette startVote(UUID rouletteId) {
        Roulette roulette = readRoulette(rouletteId);
        roulette.startVote();
        return rouletteRepository.save(roulette);
    }

    @Transactional
    public Roulette endVote(UUID rouletteId) {
        Roulette roulette = readRoulette(rouletteId);
        roulette.endVote();
        return rouletteRepository.save(roulette);
    }

    @Transactional(readOnly = true)
    public boolean hasVotingRoulette(String channelName) {
        return rouletteRepository.existsByChannelNameAndVotingIsTrue(channelName);
    }
}
