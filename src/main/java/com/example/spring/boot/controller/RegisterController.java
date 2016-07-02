package com.example.spring.boot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by pc on 2016/7/1.
 */
@RestController
public class RegisterController {
    @RequestMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterParam registerParam) {

        return ResponseEntity.ok(registerParam);
    }
}
