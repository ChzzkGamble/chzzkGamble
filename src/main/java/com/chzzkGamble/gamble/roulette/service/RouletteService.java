package com.chzzkGamble.gamble.roulette.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.chzzkGamble.chzzk.dto.DonationMessage;
import com.chzzkGamble.event.DonationEvent;
import com.chzzkGamble.exception.GambleException;
import com.chzzkGamble.exception.GambleExceptionCode;
import com.chzzkGamble.gamble.roulette.domain.Roulette;
import com.chzzkGamble.gamble.roulette.domain.RouletteElement;
import com.chzzkGamble.gamble.roulette.repository.RouletteElementRepository;
import com.chzzkGamble.gamble.roulette.repository.RouletteRepository;
import com.chzzkGamble.utils.StringParser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RouletteService {

    private static final int CHEESE_UNIT = 1_000;
    private static final long HOUR_LIMIT = 2L;
    private static final char LEFT_DELIMITER = '<';
    private static final char RIGHT_DELIMITER = '>';

    private final RouletteRepository rouletteRepository;
    private final RouletteElementRepository rouletteElementRepository;

    @Transactional
    public Roulette createRoulette(String channelId, String channelName) {
        Roulette roulette = new Roulette(channelId, channelName);
        return rouletteRepository.save(roulette);
    }

    @Transactional(readOnly = true)
    public Roulette readRoulette(UUID rouletteId) {
        return rouletteRepository.findByIdAndCreatedAtAfter(rouletteId, LocalDateTime.now().minusHours(HOUR_LIMIT))
                .orElseThrow(() -> new GambleException(GambleExceptionCode.ROULETTE_NOT_FOUND, "rouletteId : " + rouletteId));
    }

    @Transactional(readOnly = true)
    public List<RouletteElement> readRouletteElements(UUID rouletteId) {
        return rouletteElementRepository.findByRouletteId(rouletteId);
    }

    @Transactional
    @EventListener(DonationEvent.class)
    public void handleDonation(DonationEvent donationEvent) {
        DonationMessage donationMessage = (DonationMessage) donationEvent.getSource();
        vote(donationMessage.getChannelName(), donationMessage.getMsg(), donationMessage.getCheese());
    }

    @Transactional
    public void vote(String channelName, String msg, int cheese) {
        String elementName;
        try {
            elementName = StringParser.parseFromTo(msg, LEFT_DELIMITER, RIGHT_DELIMITER);
        } catch (IllegalArgumentException e) {
            return; // this is a donation not for vote
        }
        
        List<Roulette> roulettes = rouletteRepository.findByChannelNameAndVotingIsTrue(channelName);
        roulettes.forEach(roulette -> vote(roulette, elementName, cheese));
    }

    private void vote(Roulette roulette, String elementName, int cheese) {
        RouletteElement element = rouletteElementRepository.findByNameAndRouletteId(elementName, roulette.getId())
                .orElse(new RouletteElement(elementName, 0, roulette));
        
        vote(element, cheese / CHEESE_UNIT);
    }

    private void vote(RouletteElement element, int voteCount) {
        element.increaseCount(voteCount);
        rouletteElementRepository.save(element);
    }

    @Transactional
    public void startVote(UUID rouletteId) {
        Roulette roulette = readRoulette(rouletteId);
        roulette.startVote();
        rouletteRepository.save(roulette);
    }

    @Transactional
    public void endVote(UUID rouletteId) {
        Roulette roulette = readRoulette(rouletteId);
        roulette.endVote();
        rouletteRepository.save(roulette);
    }
}
