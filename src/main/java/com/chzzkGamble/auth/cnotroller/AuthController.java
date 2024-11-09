package com.chzzkGamble.auth.cnotroller;

import com.chzzkGamble.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PutMapping("/auth/update/key")
    public ResponseEntity<String> updateApiKey() {
        authService.updateApiKey();
        return ResponseEntity.ok("Api Key Updated");
    }
}
