package com.chzzkGamble.advertise.service;

import com.chzzkGamble.advertise.domain.Advertise;
import com.chzzkGamble.advertise.domain.AdvertiseMap;
import com.chzzkGamble.advertise.dto.AdvertiseCreateResponse;
import com.chzzkGamble.advertise.dto.ApprovalAdvertiseResponse;
import com.chzzkGamble.advertise.dto.NotApprovalAdvertiseResponse;
import com.chzzkGamble.advertise.repository.AdvertiseRepository;
import com.chzzkGamble.exception.AdvertiseException;
import com.chzzkGamble.exception.AdvertiseExceptionCode;
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

    private final AdvertiseRepository advertiseRepository;
    private final Clock clock;
    private AdvertiseMap advertiseMap;

    public AdvertiseService(AdvertiseRepository advertiseRepository, Clock clock) {
        this.advertiseRepository = advertiseRepository;
        this.clock = clock;
        this.advertiseMap = AdvertiseMap.from(Collections.emptyList(), clock);
    }

    public AdvertiseCreateResponse createAdvertise(Advertise advertise) {
        Advertise savedAdvertise = advertiseRepository.save(advertise);
        return new AdvertiseCreateResponse(savedAdvertise.getId());
    }

    public Advertise getAdvertise() {
        return advertiseMap.getRandom();
    }

    @Transactional
    public void approvalAdvertise(Long advertiseId) {
        Advertise advertise = advertiseRepository.findById(advertiseId)
                .orElseThrow(() -> new AdvertiseException(
                        AdvertiseExceptionCode.ADVERTISE_NOT_FOUND,
                        "advertiseId : " + advertiseId
                ));
        advertise.approval(clock);
    }

    public Map<Advertise, Double> getAdvertiseProbabilities() {
        return advertiseMap.getProbabilities();
    }

    @Transactional
    public void rejectionAdvertise(Long advertiseId) {
        Advertise advertise = advertiseRepository.findById(advertiseId)
                .orElseThrow(() -> new AdvertiseException(
                        AdvertiseExceptionCode.ADVERTISE_NOT_FOUND,
                        "advertiseId : " + advertiseId
                ));
        advertise.rejection();
    }

    public List<NotApprovalAdvertiseResponse> getNotApprovalAdvertise() {
        List<Advertise> advertises = advertiseRepository.findByActiveFalse();
        return advertises.stream()
                .map(NotApprovalAdvertiseResponse::of)
                .toList();
    }

    public List<ApprovalAdvertiseResponse> getApprovalAdvertise() {
        List<Advertise> advertises = advertiseRepository.findActiveAdvertisesWithinDate(LocalDateTime.now(clock));
        return advertises.stream()
                .map(advertise -> ApprovalAdvertiseResponse.of(advertise, advertiseMap.getProbability(advertise)))
                .toList();
    }

    @Transactional(readOnly = true)
    @Scheduled(fixedDelayString = "${advertise.update-interval}")
    public void updateAdvertiseMap() {
        List<Advertise> validAdvertise = advertiseRepository.findActiveAdvertisesWithinDate(LocalDateTime.now(clock));
        advertiseMap = AdvertiseMap.from(validAdvertise, clock);

        logger.info("updated : {}", advertiseMap);
    }
}
