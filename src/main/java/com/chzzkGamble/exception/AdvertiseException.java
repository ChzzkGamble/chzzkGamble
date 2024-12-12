package com.chzzkGamble.exception;

import com.chzzkGamble.exception.handler.ChzOnMeException;
import lombok.Getter;

@Getter
public class AdvertiseException extends ChzOnMeException {

    public AdvertiseException(AdvertiseExceptionCode exceptionCode) {
        this(exceptionCode, "");
    }

    public AdvertiseException(AdvertiseExceptionCode exceptionCode, String supplementaryMessage) {
        super(exceptionCode, supplementaryMessage);
    }
}
