package com.example.spring.boot.service;

/**
 * Created by pc on 2016/7/2.
 */
public interface CaptchaService {
    String createRegisterCaptcha(String username);

    boolean isRegisterCaptchaAvailable(String username, String captcha);

    void removeRegisterCaptcha(String username);
}
