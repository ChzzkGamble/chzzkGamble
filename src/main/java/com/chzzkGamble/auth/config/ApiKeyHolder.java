package com.chzzkGamble.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiKeyHolder {

    @Getter
    @Setter
    private static String apiKey;
}
