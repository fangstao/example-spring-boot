package com.example.spring.boot.service;

import com.example.spring.boot.domain.User;

/**
 * Created by pc on 2016/7/2.
 */
public interface UserService {
    User register(String username, String password) throws RegisterException;

    User findByUsername(String username);
}
