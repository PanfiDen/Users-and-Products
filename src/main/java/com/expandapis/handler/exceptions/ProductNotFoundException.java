package com.expandapis.handler.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException() {
        super("Product with such credentials is not found");
    }
}