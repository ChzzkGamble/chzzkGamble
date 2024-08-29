package com.chzzkGamble.exception;

import com.chzzkGamble.exception.dto.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChzzkException.class)
    public ResponseEntity<ExceptionResponse> handleChzzkException(ChzzkException e) {
        ChzzkExceptionCode exceptionCode = e.getExceptionCode();

        ExceptionResponse response = new ExceptionResponse(
                exceptionCode.getCode(),
                exceptionCode.getMessage()
        );
        return ResponseEntity.status(exceptionCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(GambleException.class)
    public ResponseEntity<ExceptionResponse> handleGambleException(GambleException e) {
        GambleExceptionCode exceptionCode = e.getExceptionCode();

        ExceptionResponse response = new ExceptionResponse(
                exceptionCode.getCode(),
                exceptionCode.getMessage()
        );
        return ResponseEntity.status(exceptionCode.getHttpStatus()).body(response);
    }
}
