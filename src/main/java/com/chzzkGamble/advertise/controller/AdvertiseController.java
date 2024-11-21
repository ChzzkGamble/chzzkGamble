package com.chzzkGamble.advertise.controller;

import com.chzzkGamble.advertise.domain.Advertise;
import com.chzzkGamble.advertise.dto.AdvertiseProbabilityResponse;
import com.chzzkGamble.advertise.dto.AdvertiseProbabilityResponses;
import com.chzzkGamble.advertise.dto.AdvertiseRequest;
import com.chzzkGamble.advertise.dto.AdvertiseResponse;
import com.chzzkGamble.advertise.service.AdvertiseService;
import com.chzzkGamble.auth.config.RequireApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Comparator;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdvertiseController {

    private final AdvertiseService advertiseService;

    @RequireApiKey
    @PostMapping("/advertise")
    public ResponseEntity<Void> createAdvertise(@RequestBody AdvertiseRequest request) {
        Advertise advertise = advertiseService.createAdvertise(request.toEntity());
        return ResponseEntity.created(URI.create("advertise/" + advertise.getId())).build();
    }

    @GetMapping("/advertise")
    public ResponseEntity<AdvertiseResponse> getAdvertise() {
        Advertise advertise = advertiseService.getAdvertise();
        return ResponseEntity.ok(AdvertiseResponse.from(advertise));
    }

    @RequireApiKey
    @PutMapping("/advertise/approval")
    public ResponseEntity<Void> approvalAdvertise(@RequestBody Long advertiseId) {
        advertiseService.approvalAdvertise(advertiseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/advertise/probabilities")
    public ResponseEntity<AdvertiseProbabilityResponses> getProbabilities() {
        Map<Advertise, Double> advertiseProbabilities = advertiseService.getAdvertiseProbabilities();

        AdvertiseProbabilityResponses responses = new AdvertiseProbabilityResponses(
                advertiseProbabilities.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getValue() * -1))
                .map(entry -> AdvertiseProbabilityResponse.from(entry.getKey(), entry.getValue()))
                .toList());

        return ResponseEntity.ok(responses);
    }
}
