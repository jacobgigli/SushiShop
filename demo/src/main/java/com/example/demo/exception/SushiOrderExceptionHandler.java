package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class SushiOrderExceptionHandler {
    @ExceptionHandler(value = {})
    public ResponseEntity<Object> handleSushiOrderRequestException(SushiOrderRequestException e) {
        SushiOrderException sushiOrderException = new SushiOrderException(e.getMessage(), e, HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Z")));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 1);
        map.put("msg", sushiOrderException.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);


    }
}

