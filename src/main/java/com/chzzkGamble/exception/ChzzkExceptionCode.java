package com.chzzkGamble.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChzzkExceptionCode {

    // api 1_xxx

    // chat 2_xxx
    CHAT_IS_DISCONNECTED(HttpStatus.INTERNAL_SERVER_ERROR, 2_001, "채팅방과 연결이 끊겨있습니다."),
    CHAT_CONNECTION(HttpStatus.INTERNAL_SERVER_ERROR,2_002, "채팅방 연결에 실패했습니다"),
    CHAT_DISCONNECTION(HttpStatus.INTERNAL_SERVER_ERROR,2_003, "채팅방 연결 종료에 실패했습니다."),

    // json 3_xxx,
    JSON_CONVERT(HttpStatus.INTERNAL_SERVER_ERROR,3_001, "JSON 변환 중 오류가 생겼습니다."),
    JSON_PARSING(HttpStatus.INTERNAL_SERVER_ERROR,3_002, "JSON 파싱 중 오류가 생겼습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ChzzkExceptionCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
