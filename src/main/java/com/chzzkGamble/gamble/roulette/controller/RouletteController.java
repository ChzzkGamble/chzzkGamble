package com.chzzkGamble.gamble.roulette.controller;

import com.chzzkGamble.gamble.roulette.domain.Roulette;
import com.chzzkGamble.gamble.roulette.domain.RouletteElement;
import com.chzzkGamble.gamble.roulette.dto.RouletteElementResponse;
import com.chzzkGamble.gamble.roulette.service.RouletteService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roulette")
@RequiredArgsConstructor
public class RouletteController {

    private final RouletteService rouletteService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoulette(@RequestBody @Valid RouletteCreateRequest request) {
        Roulette roulette = rouletteService.createRoulette(request.getChannelName());
        ResponseCookie cookie = ResponseCookie.from("rouletteId", roulette.getId().toString())
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(5 * 60 * 60L) // 5 hours
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start(@CookieValue(name = "rouletteId") Cookie cookie) {
        // TODO : check connection is established
        rouletteService.startVote(UUID.fromString(cookie.getValue()));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<RouletteElementResponse> readRoulette(@CookieValue(name = "rouletteId") Cookie cookie) {
        UUID rouletteId = UUID.fromString(cookie.getValue());
        List<RouletteElement> rouletteElements = rouletteService.readRouletteElements(rouletteId);

        int totalVote = rouletteElements.stream()
                .mapToInt(RouletteElement::getCount)
                .sum();

        return rouletteElements
                .stream()
                .map(element -> RouletteElementResponse.of(element, totalVote))
                .toList();
    }
}
