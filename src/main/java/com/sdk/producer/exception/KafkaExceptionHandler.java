package com.sdk.producer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class KafkaExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentException(MethodArgumentNotValidException ex){
        log.info("MethodArgumentNotValidException");
        String exceptions = ex.getBindingResult().getFieldErrors().stream()
                .map(element -> element.getField() + " - " + element.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(","));

        log.info("Error while binding Request body {} ",  exceptions);
        return new ResponseEntity<>(exceptions, HttpStatus.BAD_REQUEST);
    }
}
