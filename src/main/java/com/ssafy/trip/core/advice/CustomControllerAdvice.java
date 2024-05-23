package com.ssafy.trip.core.advice;

import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception exception) {
        log.error("Exception: {}", exception.getMessage());
        return ErrorResponse.of(exception);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleSQLException(SQLException exception) {
        log.error("SQLException: {}", exception.getMessage());
        return ErrorResponse.of(new CustomException(ErrorCode.SERVER_ERROR));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        log.error("CustomException: {}", exception.getMessage());
        return ResponseEntity.status(exception.getErrorCode().getStatus())
                .body(ErrorResponse.of(exception));
    }
}
