package com.example.spring.boot.service;

/**
 * Created by fangtao on 16/7/2.
 */
public class SendMessageTooManyTimesException extends MessageException {
    public SendMessageTooManyTimesException(String message) {
        super(message);
    }
}
