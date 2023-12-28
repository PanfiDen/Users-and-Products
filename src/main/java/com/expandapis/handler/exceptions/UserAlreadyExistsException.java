package com.expandapis.handler.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("This username is already taken");
    }
}
