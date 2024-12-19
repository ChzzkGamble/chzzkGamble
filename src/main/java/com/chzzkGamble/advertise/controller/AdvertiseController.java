package com.chzzkGamble.advertise.controller;

import com.chzzkGamble.advertise.domain.Advertise;
import com.chzzkGamble.advertise.dto.*;
import com.chzzkGamble.advertise.service.AdvertiseService;
import com.chzzkGamble.auth.config.RequireApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdvertiseController {

    private final AdvertiseService advertiseService;

    @RequireApiKey
    @PostMapping("/advertise")
    public ResponseEntity<AdvertiseCreateResponse> createAdvertise(@RequestBody AdvertiseCreateRequest request) {
        AdvertiseCreateResponse response = advertiseService.createAdvertise(request.toEntity());
        return ResponseEntity
                .created(URI.create("advertise/" + response.id()))
                .body(response);
    }

    @GetMapping("/advertise")
    public ResponseEntity<AdvertiseImageResponse> getAdvertise() {
        Advertise advertise = advertiseService.getAdvertise();
        return ResponseEntity.ok(AdvertiseImageResponse.from(advertise));
    }

    @RequireApiKey
    @GetMapping("advertises/not-approval")
    public ResponseEntity<List<NotApprovalAdvertiseResponse>> getNotApprovalAdvertises() {
        List<NotApprovalAdvertiseResponse> responses = advertiseService.getNotApprovalAdvertise();
        return ResponseEntity.ok().body(responses);
    }

    @RequireApiKey
    @GetMapping("advertises/approval")
    public ResponseEntity<List<ApprovalAdvertiseResponse>> getApprovalAdvertises() {
        List<ApprovalAdvertiseResponse> responses = advertiseService.getApprovalAdvertise();
        return ResponseEntity.ok().body(responses);
    }

    @RequireApiKey
    @PutMapping("/advertise/approval")
    public ResponseEntity<Void> approvalAdvertise(@RequestBody ApprovalRequest request) {
        advertiseService.approvalAdvertise(request.id());
        return ResponseEntity.ok().build();
    }

    @RequireApiKey
    @PutMapping("/advertise/rejection")
    public ResponseEntity<Void> rejectionAdvertise(@RequestBody RejectionRequest request) {
        advertiseService.rejectionAdvertise(request.id());
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
