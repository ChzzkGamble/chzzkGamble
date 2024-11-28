package com.chzzkGamble.exception;

import lombok.Getter;

@Getter
public class AdvertiseException extends RuntimeException {

    private final AdvertiseExceptionCode exceptionCode;
    private final String supplementaryMessage;

    public AdvertiseException(AdvertiseExceptionCode exceptionCode) {
        this(exceptionCode, "");
    }

    public AdvertiseException(AdvertiseExceptionCode exceptionCode, String supplementaryMessage) {
        super();
        this.exceptionCode = exceptionCode;
        this.supplementaryMessage = supplementaryMessage;
    }

    @Override
    public String getMessage() {
        return exceptionCode.getMessage();
    }
}
