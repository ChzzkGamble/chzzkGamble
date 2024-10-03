package com.chzzkGamble.gamble.roulette.controller;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.chat.dto.ChatConnectRequest;
import com.chzzkGamble.chzzk.chat.service.ChzzkChatService;
import com.chzzkGamble.chzzk.dto.ChannelInfoApiResponse;
import com.chzzkGamble.gamble.roulette.domain.Roulette;
import com.chzzkGamble.gamble.roulette.domain.RouletteElement;
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

    private final RouletteService rouletteService;
    private final ChzzkApiService chzzkApiService;
    private final ChzzkChatService chzzkChatService;

    @PostMapping("/create")
    public ResponseEntity<ChannelInfoApiResponse> createRoulette(@RequestBody ChatConnectRequest request) {
        ChannelInfoApiResponse channelInfo = chzzkApiService.getChannelInfo(request.getChannelName());
        String channelName = channelInfo.getChannelName();

        Roulette roulette = rouletteService.createRoulette(channelName);
        ResponseCookie cookie = ResponseCookie.from("rouletteId", roulette.getId().toString())
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .maxAge(5 * 60 * 60L) // 5 hours
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(channelInfo);
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start(@CookieValue(name = "rouletteId") Cookie cookie) {
        // TODO : check connection is established
        rouletteService.startVote(UUID.fromString(cookie.getValue()));
        return ResponseEntity.ok().build();
    }
    // TODO 1. check api 유지할 지 결정
    // TODO 2. 유지한다면 request 내 channelName 함께 담도록 수정
//    @GetMapping("/check")
//    public ResponseEntity<Void> checkConnection(@CookieValue(name = "rouletteId") Cookie cookie) {
//        if (chzzkChatService.isConnected(UUID.fromString(cookie.getValue()))) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.notFound().build();
//    }

    @PostMapping("/end")
    public ResponseEntity<Void> end(@CookieValue(name = "rouletteId") Cookie cookie) {
        UUID rouletteId = UUID.fromString(cookie.getValue());
        Roulette roulette = rouletteService.endVote(rouletteId);
        if (!rouletteService.hasVotingRoulette(roulette.getChannelName())) {
            chzzkChatService.disconnectChatRoom(roulette.getChannelName());
        }

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
