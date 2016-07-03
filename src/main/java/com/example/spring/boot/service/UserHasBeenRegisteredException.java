package com.example.spring.boot.service;


public class UserHasBeenRegisteredException extends RegisterException {
    public UserHasBeenRegisteredException(String message) {
        super(message);
    }
}
