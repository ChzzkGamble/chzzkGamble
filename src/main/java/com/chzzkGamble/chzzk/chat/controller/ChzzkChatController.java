package com.chzzkGamble.chzzk.chat.controller;

import com.chzzkGamble.chzzk.api.ChzzkApiService;
import com.chzzkGamble.chzzk.chat.dto.ChatConnectRequest;
import com.chzzkGamble.chzzk.chat.service.ChzzkChatService;
import com.chzzkGamble.chzzk.dto.ChannelInfoApiResponse;
import com.chzzkGamble.exception.ChzzkException;
import com.chzzkGamble.exception.ChzzkExceptionCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChzzkChatController {

    private final ChzzkChatService chzzkChatService;
    private final ChzzkApiService chzzkApiService;
    private final RedissonClient redissonClient;

    @PostMapping("/connect")
    public ResponseEntity<?> connect(@RequestBody @Valid ChatConnectRequest request) {
        String channelName = request.getChannelName();
        ChannelInfoApiResponse response = chzzkApiService.getChannelInfo(channelName);

        if (!response.isOpenLive()) {
            throw new ChzzkException(ChzzkExceptionCode.CHANNEL_LIVE_CLOSED, "channel : " + response.getChannelName());
        }
        if (!response.getChannelName().equals(channelName)) {
            throw new ChzzkException(ChzzkExceptionCode.CHANNEL_NAME_INVALID, "channel : " + response.getChannelName());
        }
        chzzkChatService.connectChatRoom(channelName);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Void> checkConnection(@RequestParam String channelName) {
        if (chzzkChatService.isConnected(channelName)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/connections")
    public ResponseEntity<List<String>> getCurrentConnections() {
        List<String> channelNames = new ArrayList<>();
        RKeys keys = redissonClient.getKeys();
        keys.getKeys().forEach(channelNames::add);

        return ResponseEntity.ok(channelNames);
    }
}
