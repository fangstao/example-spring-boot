package com.example.spring.boot.service;


public class CreateOrderException extends Exception {
    public CreateOrderException() {
    }

    public CreateOrderException(String message) {
        super(message);
    }
}
