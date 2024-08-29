package com.chzzkGamble.exception.dto;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    int code;
    String message;

    public ExceptionResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
