package com.example.spring.boot.service;

import com.example.spring.boot.service.CreateOrderException;

public class InsufficientProductStockException extends CreateOrderException {
    public InsufficientProductStockException(String message) {
        super(message);
    }
}
