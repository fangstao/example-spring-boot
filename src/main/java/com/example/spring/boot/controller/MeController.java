package com.example.spring.boot.controller;

import com.example.spring.boot.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    @RequestMapping("/me")
    public User me(User user) {
        return user;
    }
}
