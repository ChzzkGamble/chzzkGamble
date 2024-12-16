package com.chzzkGamble.exception;

import com.chzzkGamble.exception.handler.ChzOnMeException;
import lombok.Getter;

@Getter
public class GambleException extends ChzOnMeException {

    public GambleException(GambleExceptionCode exceptionCode) {
        this(exceptionCode, "");
    }

    public GambleException(GambleExceptionCode exceptionCode, String supplementaryMessage) {
        super(exceptionCode, supplementaryMessage);
    }
}
