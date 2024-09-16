package com.chzzkGamble.advertise.service;

import com.chzzkGamble.advertise.domain.Advertise;
import com.chzzkGamble.advertise.domain.AdvertiseMap;
import com.chzzkGamble.advertise.repository.AdvertiseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AdvertiseService {

    private static final Logger logger = LoggerFactory.getLogger(AdvertiseService.class);
    private static final long ADVERTISE_DURATION = 10L; // 10 days

    private final AdvertiseRepository advertiseRepository;
    private final Clock clock;
    private AdvertiseMap advertiseMap;

    public AdvertiseService(AdvertiseRepository advertiseRepository, Clock clock) {
        this.advertiseRepository = advertiseRepository;
        this.clock = clock;
        this.advertiseMap = AdvertiseMap.from(Collections.emptyList());
    }

    public Advertise createAdvertise(Advertise advertise) {
        return advertiseRepository.save(advertise);
    }

    public Advertise getAdvertise() {
        return advertiseMap.getRandom();
    }

    public Map<Advertise, Double> getAdvertiseProbabilities() {
        return advertiseMap.getProbabilities();
    }

    @Transactional(readOnly = true)
    @Scheduled(fixedDelayString = "${advertise.update-interval}")
    public void updateAdvertiseMap() {
        List<Advertise> validAdvertise = advertiseRepository.findByCreatedAtAfterAndApprovedIsTrue(
                LocalDateTime.now(clock).minusDays(ADVERTISE_DURATION));
        advertiseMap = AdvertiseMap.from(validAdvertise);

        logger.info("updated : {}", advertiseMap);
    }
}
