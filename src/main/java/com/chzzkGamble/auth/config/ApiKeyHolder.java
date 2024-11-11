package com.chzzkGamble.auth.config;

import lombok.Setter;

public class ApiKeyHolder {

    @Setter
    private static String apiKey;

    public static boolean validateApiKey(String input) {
        return apiKey.equals(input);
    }
}
