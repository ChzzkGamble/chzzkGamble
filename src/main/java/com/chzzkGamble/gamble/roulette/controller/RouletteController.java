package com.chzzkGamble.gamble.roulette.controller;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.chat.ChzzkChatService;
import com.chzzkGamble.chzzk.dto.ChannelInfoApiResponse;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import com.chzzkGamble.gamble.roulette.domain.Roulette;
import com.chzzkGamble.gamble.roulette.domain.RouletteElement;
import com.chzzkGamble.gamble.roulette.dto.RouletteCreateRequest;
import com.chzzkGamble.gamble.roulette.dto.RouletteElementResponse;
import com.chzzkGamble.gamble.roulette.service.RouletteService;
import jakarta.servlet.http.Cookie;
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

    private static final String INVALID_CHANNEL_NAME = "(알 수 없음)";

    private final RouletteService rouletteService;
    private final ChzzkApiService chzzkApiService;
    private final ChzzkChatService chzzkChatService;

    @PostMapping("/create")
    public ResponseEntity<ChannelInfoApiResponse> createRoulette(@RequestBody RouletteCreateRequest request) {
        String channelId = request.getChannelId();

        ChannelInfoApiResponse channelInfo = chzzkApiService.getChannelInfo(channelId);
        String channelName = channelInfo.getChannelName();
        if (channelName.equals(INVALID_CHANNEL_NAME)) {
            throw new ChzzkException(ChzzkExceptionCode.CHANNEL_ID_INVALID, "channelId : " + channelId);
        }

        Roulette roulette = rouletteService.createRoulette(channelId, channelName);
        ResponseCookie cookie = ResponseCookie.from("rouletteId", roulette.getId().toString())
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(2 * 60 * 60L) // 2 hours
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(channelInfo);
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start(@CookieValue(name = "rouletteId") Cookie cookie) {
        Roulette roulette = rouletteService.readRoulette(UUID.fromString(cookie.getValue()));
        chzzkChatService.connectChatRoom(roulette.getChannelId(), roulette.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Void> checkConnection(@CookieValue(name = "rouletteId") Cookie cookie) {
        if (chzzkChatService.isConnected(UUID.fromString(cookie.getValue()))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/end")
    public ResponseEntity<Void> end(@CookieValue(name = "rouletteId") Cookie cookie) {
        UUID rouletteId = UUID.fromString(cookie.getValue());
        chzzkChatService.disconnectChatRoom(rouletteId);
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
