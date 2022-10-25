package com.example.demo.exception;

public class SushiOrderRequestException extends RuntimeException {
    public SushiOrderRequestException(String message) {
        super(message);
    }

    public SushiOrderRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
