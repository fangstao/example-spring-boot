package com.example.spring.boot.service;


public interface CaptchaService {
    String createRegisterCaptcha(String username);

    boolean isRegisterCaptchaAvailable(String username, String captcha);

    void removeRegisterCaptcha(String username);
}
