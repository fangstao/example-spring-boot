package com.example.spring.boot.service;


import com.example.spring.boot.domain.Message;

public interface MessageService {

    void send(Message message) throws MessageException;
}
