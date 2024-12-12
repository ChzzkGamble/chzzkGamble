package com.chzzkGamble.exception.handler;

import org.springframework.http.HttpStatus;

public interface ChzOnMeExceptionCode {

    String getMessage();

    int getCode();

    HttpStatus getHttpStatus();
}
