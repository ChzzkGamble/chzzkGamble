package com.chzzkGamble.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Getter
public enum ChzzkExceptionCode {

    // api 1_xxx
    CHANNEL_ID_INVALID(BAD_REQUEST, 1_001, "유효하지 않은 채널 ID 입니다."),

    // chat 2_xxx
    CHAT_IS_CONNECTED(BAD_REQUEST, 2_001, "채팅방과 이미 연결되어 있습니다."),
    CHAT_IS_DISCONNECTED(BAD_REQUEST, 2_002, "채팅방과 이미 연결이 끊겨있습니다."),
    CHAT_CONNECTION_ERROR(INTERNAL_SERVER_ERROR,2_003, "채팅방 연결에 실패했습니다"),
    CHAT_DISCONNECTION_ERROR(INTERNAL_SERVER_ERROR,2_004, "채팅방 연결 종료에 실패했습니다."),
    CHAT_CONNECTION_LIMIT(SERVICE_UNAVAILABLE, 2_005, "채팅방 최대 연결 수를 초과했습니다."),

    // json 3_xxx
    JSON_CONVERT(INTERNAL_SERVER_ERROR,3_001, "JSON 변환 중 오류가 생겼습니다."),
    JSON_PARSING(INTERNAL_SERVER_ERROR,3_002, "JSON 파싱 중 오류가 생겼습니다."),
    ;
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ChzzkExceptionCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
