package com.chzzkGamble.exception;

import lombok.Getter;

@Getter
public enum GambleExceptionCode {

    // roulette 1_xxx
    ROULETTE_NOT_FOUND(1_001, "룰렛을 찾을 수 없습니다."),

    // rouletteElement 2_xxx
    ROULETTE_ELEMENT_NOT_FOUND(2_001, "룰렛 요소를 찾을 수 없습니다."),
    ROULETTE_ELEMENT_INCREASE(2_002, "잘못된 상승 수치입니다."),

    ;

    private final int code;
    private final String message;

    GambleExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
