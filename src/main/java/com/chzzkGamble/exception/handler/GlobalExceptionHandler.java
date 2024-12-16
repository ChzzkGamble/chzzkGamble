package com.chzzkGamble.exception.handler;

import com.chzzkGamble.exception.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ChzOnMeException.class)
    public ResponseEntity<ExceptionResponse> handleChzzkException(ChzOnMeException e) {
        logger.error(e.getMessage());
        logSupplementaryMessage(e);

        ChzOnMeExceptionCode exceptionCode = e.getExceptionCode();

        ExceptionResponse response = new ExceptionResponse(
                exceptionCode.getCode(),
                exceptionCode.getMessage()
        );
        return ResponseEntity.status(exceptionCode.getHttpStatus()).body(response);
    }

    private void logSupplementaryMessage(ChzOnMeException e) {
        String supplementaryMessage = e.getSupplementaryMessage();
        if (!supplementaryMessage.isBlank()) {
            logger.error(e.getSupplementaryMessage());
        }
    }
}
