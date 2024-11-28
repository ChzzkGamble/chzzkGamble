package com.chzzkGamble.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdvertiseExceptionCode {

    ADVERTISE_NOT_FOUND(HttpStatus.NOT_FOUND, 1_001, "광고를 찾을 수 없습니다."),

    INVALID_AD_PERIOD(HttpStatus.BAD_REQUEST, 2_001, "광고 기한은 1 ~ 30내에서만 가능합니다."),
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    AdvertiseExceptionCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
