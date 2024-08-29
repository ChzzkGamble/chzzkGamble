package com.chzzkGamble.gamble.controller;

import com.chzzkGamble.gamble.domain.Roulette;
import com.chzzkGamble.gamble.dto.ElementCreateRequest;
import com.chzzkGamble.gamble.dto.RouletteCreateRequest;
import com.chzzkGamble.gamble.dto.RouletteElementResponse;
import com.chzzkGamble.gamble.dto.RouletteResponse;
import com.chzzkGamble.gamble.service.RouletteService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RouletteController {

    private final RouletteService rouletteService;

    @PostMapping("/start")
    public ResponseEntity<Void> start(@RequestBody RouletteCreateRequest request) {
        String channelId = request.getChannelId();
        Roulette roulette = rouletteService.createRoulette(channelId);
        ResponseCookie cookie = ResponseCookie.from("rouletteId", roulette.getId().toString())
                .httpOnly(true)
                .maxAge(2 * 60 * 60L)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/element")
    public ResponseEntity<Void> addElement(@CookieValue(name = "rouletteId") Cookie cookie,
                                           @RequestBody ElementCreateRequest request) {
        UUID rouletteId = UUID.fromString(cookie.getValue());
        rouletteService.addElements(rouletteId, request.getElements());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<RouletteResponse> readRoulette(@CookieValue(name = "rouletteId") Cookie cookie) {
        UUID rouletteId = UUID.fromString(cookie.getValue());
        List<RouletteElementResponse> elementResponses = rouletteService.readRouletteElements(rouletteId)
                .stream()
                .map(RouletteElementResponse::from)
                .toList();

        return ResponseEntity.ok(new RouletteResponse(elementResponses));
    }
}
