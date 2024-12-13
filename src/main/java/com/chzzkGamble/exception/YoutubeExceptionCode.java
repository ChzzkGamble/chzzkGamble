package com.chzzkGamble.exception;

import com.chzzkGamble.exception.handler.ChzOnMeExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public enum YoutubeExceptionCode implements ChzOnMeExceptionCode {

    // youtube api 1_xxx
    YOUTUBE_TITLE_INVALID(INTERNAL_SERVER_ERROR, 1_001, "제목과 일치하는 유튜브 영상을 찾아오지 못했습니다."),
    YOUTUBE_API_INVALID(INTERNAL_SERVER_ERROR, 1_002, "유튜브 서버에서 정보를 찾아올 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    YoutubeExceptionCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
