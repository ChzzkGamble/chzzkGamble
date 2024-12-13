package com.chzzkGamble.exception;

import com.chzzkGamble.exception.handler.ChzOnMeException;

public class YoutubeException extends ChzOnMeException {

    public YoutubeException(YoutubeExceptionCode exceptionCode) {
        this(exceptionCode, "");
    }

    public YoutubeException(YoutubeExceptionCode exceptionCode, String supplementaryMessage) {
        super(exceptionCode, supplementaryMessage);
    }
}
