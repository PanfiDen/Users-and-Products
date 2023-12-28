package com.expandapis.handler.exceptions;

public class ForbiddenRequestException extends RuntimeException {
    public ForbiddenRequestException() {
        super("You don't have sufficient rights to perform this action");
    }
}
