package com.chzzkGamble.exception;

public class GambleException extends RuntimeException {

    private final GambleExceptionCode exceptionCode;
    private final String supplementaryMessage;

    public GambleException(GambleExceptionCode exceptionCode) {
        this(exceptionCode, "");
    }

    public GambleException(GambleExceptionCode exceptionCode, String supplementaryMessage) {
        super();
        this.exceptionCode = exceptionCode;
        this.supplementaryMessage = supplementaryMessage;
    }

    public String getMessage() {
        return exceptionCode.getMessage() + System.lineSeparator() + supplementaryMessage;
    }
}
