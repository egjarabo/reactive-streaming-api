package com.egjarabo.streaming.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String ERRORS = "errors";

    // Handles validation errors (@Valid failures)
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationErrors(
            WebExchangeBindException ex) {

        Map<String, Object> error = Map.of(
                TIMESTAMP, LocalDateTime.now(),
                STATUS, HttpStatus.BAD_REQUEST.value(),
                ERRORS, ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(e -> e.getField() + ": " + e.getDefaultMessage())
                        .toList()
        );

        return Mono.just(ResponseEntity.badRequest().body(error));
    }

    // Handles resource not found errors
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleNotFound(
            ResourceNotFoundException ex) {

        Map<String, Object> error = Map.of(
                TIMESTAMP, LocalDateTime.now(),
                STATUS, HttpStatus.NOT_FOUND.value(),
                MESSAGE, ex.getMessage()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    // Handles duplicate resource errors
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgument(
            IllegalArgumentException ex) {

        Map<String, Object> error = Map.of(
                TIMESTAMP, LocalDateTime.now(),
                STATUS, HttpStatus.CONFLICT.value(),
                MESSAGE, ex.getMessage()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }
}