package com.chzzkGamble.exception.handler;

import lombok.Getter;

@Getter
public class ChzOnMeException extends RuntimeException {

    private final ChzOnMeExceptionCode exceptionCode;
    private final String supplementaryMessage;

    public ChzOnMeException(ChzOnMeExceptionCode exceptionCode) {
        this(exceptionCode, "");
    }

    public ChzOnMeException(ChzOnMeExceptionCode exceptionCode, String supplementaryMessage) {
        super();
        this.exceptionCode = exceptionCode;
        this.supplementaryMessage = supplementaryMessage;
    }

    @Override
    public String getMessage() {
        return exceptionCode.getMessage();
    }
}
