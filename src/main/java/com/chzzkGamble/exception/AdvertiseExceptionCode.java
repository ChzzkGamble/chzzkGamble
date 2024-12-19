package com.chzzkGamble.exception;

import com.chzzkGamble.advertise.domain.Advertise;
import com.chzzkGamble.exception.handler.ChzOnMeExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdvertiseExceptionCode implements ChzOnMeExceptionCode {

    ADVERTISE_NOT_FOUND(HttpStatus.NOT_FOUND, 1_001, "광고를 찾을 수 없습니다."),

    INVALID_AD_PERIOD(HttpStatus.BAD_REQUEST, 2_001, String.format(
            "광고 기한은 %d ~ %d내에서만 가능합니다.",
            Advertise.MIN_AD_PERIOD,
            Advertise.MAX_AD_PERIOD
    )),
    ALREADY_APPROVED_AD(HttpStatus.BAD_REQUEST, 2_002, "이미 승인된 광고입니다."),
    AD_NOT_APPROVED(HttpStatus.BAD_REQUEST, 2_003, "아직 승인되지 않은 광고입니다."),
    COST_UNDER_ZERO(HttpStatus.BAD_REQUEST, 2_004, "광고 금액은 양수여야 합니다."),
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
