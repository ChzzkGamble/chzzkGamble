package com.chzzkGamble.exception;

import com.chzzkGamble.exception.handler.ChzOnMeException;

public class ChzzkException extends ChzOnMeException {

    public ChzzkException(ChzzkExceptionCode exceptionCode) {
        this(exceptionCode, "");
    }

    public ChzzkException(ChzzkExceptionCode exceptionCode, String supplementaryMessage) {
        super(exceptionCode, supplementaryMessage);
    }
}
