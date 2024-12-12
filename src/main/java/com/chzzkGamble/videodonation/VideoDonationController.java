package com.chzzkGamble.videodonation;

import com.chzzkGamble.videodonation.service.VideoDonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video-donations")
@RequiredArgsConstructor
public class VideoDonationController {

    private final VideoDonationService videoDonationService;

    @GetMapping
    public ResponseEntity<?> readVideoDonations(@RequestParam String channelName) {
        return ResponseEntity.ok(videoDonationService.getRecentlyVideoDonation(channelName));
    }
}
