package com.expandapis.handler.exceptions;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
        super("You are not authorised");
    }
}
