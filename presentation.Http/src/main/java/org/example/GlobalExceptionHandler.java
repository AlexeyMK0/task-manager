package org.example;

import org.example.Exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> GenericExceptionHandler(Exception exception) {
        logger.error("processing system exception", exception);
        var responseDto = new ErrorResponseDto(
                "Internal server exception",
                exception.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseDto);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDto> AppExceptionHandler(AppException exception) {
        logger.error("processing AppException", exception);

        var responseDto = new ErrorResponseDto(
                "Application exception",
                exception.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseDto);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> TaskNotFoundHandler(TaskNotFoundException exception) {
        logger.error("processing TaskNotFoundException", exception);

        var responseDto = new ErrorResponseDto(
                "Task not found",
                exception.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseDto);
    }

    @ExceptionHandler(TaskOperationException.class)
    public ResponseEntity<ErrorResponseDto> TaskOperationExceptionHandler(TaskOperationException exception) {
        logger.error("processing TaskOperationException", exception);

        var responseDto = new ErrorResponseDto(
                "Operation failure",
                exception.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseDto);
    }
}
