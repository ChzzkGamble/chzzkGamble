package com.chzzkGamble.exception;

public class ChzzkException extends RuntimeException {

    private final ChzzkExceptionCode exceptionCode;
    private final String supplementaryMessage;

    public ChzzkException(ChzzkExceptionCode exceptionCode) {
        this(exceptionCode, "");
    }

    public ChzzkException(ChzzkExceptionCode exceptionCode, String supplementaryMessage) {
        super();
        this.exceptionCode = exceptionCode;
        this.supplementaryMessage = supplementaryMessage;
    }

    @Override
    public String getMessage() {
        return exceptionCode.getMessage() + System.lineSeparator() + supplementaryMessage;
    }
}
