package com.chzzkGamble.exception;

import com.chzzkGamble.exception.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ChzzkException.class)
    public ResponseEntity<ExceptionResponse> handleChzzkException(ChzzkException e) {
        logger.error(e.getMessage());
        logger.error(e.getSupplementaryMessage());
        ChzzkExceptionCode exceptionCode = e.getExceptionCode();

        ExceptionResponse response = new ExceptionResponse(
                exceptionCode.getCode(),
                exceptionCode.getMessage()
        );
        return ResponseEntity.status(exceptionCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(GambleException.class)
    public ResponseEntity<ExceptionResponse> handleGambleException(GambleException e) {
        logger.error(e.getMessage());
        logger.error(e.getSupplementaryMessage());

        GambleExceptionCode exceptionCode = e.getExceptionCode();

        ExceptionResponse response = new ExceptionResponse(
                exceptionCode.getCode(),
                exceptionCode.getMessage()
        );
        return ResponseEntity.status(exceptionCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(AdvertiseException.class)
    public ResponseEntity<ExceptionResponse> handleAdvertiseException(AdvertiseException e) {
        logger.error(e.getMessage());
        logger.error(e.getSupplementaryMessage());
        AdvertiseExceptionCode exceptionCode = e.getExceptionCode();

        ExceptionResponse response = new ExceptionResponse(
                exceptionCode.getCode(),
                exceptionCode.getMessage()
        );
        return ResponseEntity.status(exceptionCode.getHttpStatus()).body(response);
    }
}
