package ru.itmo.lab1.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpStatusCodeException.class)
    @ResponseBody
    public ResponseEntity<?> handleHttpException(HttpStatusCodeException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
    }

}
