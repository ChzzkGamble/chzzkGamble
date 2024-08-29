package com.chzzkGamble.gamble.controller;

import com.chzzkGamble.gamble.domain.Roulette;
import com.chzzkGamble.gamble.dto.RouletteCreateRequest;
import com.chzzkGamble.gamble.service.RouletteService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RouletteController {

    private final RouletteService rouletteService;

    @PostMapping("/start")
    public ResponseEntity<Void> start(@RequestBody RouletteCreateRequest request) {
        String channelId = request.getChannelId();
        Roulette roulette = rouletteService.createRoulette(channelId);
        Cookie cookie = new Cookie("rouletteId", roulette.getId().toString());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}
