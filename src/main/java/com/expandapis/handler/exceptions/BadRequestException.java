package com.expandapis.handler.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String exception) {
        super(exception);
    }
}