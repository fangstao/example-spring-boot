package com.example.spring.boot.repository;

import com.example.spring.boot.domain.Message;

/**
 * Created by pc on 2016/7/2.
 */
public interface MessageRepository {
    void save(Message message);
}
