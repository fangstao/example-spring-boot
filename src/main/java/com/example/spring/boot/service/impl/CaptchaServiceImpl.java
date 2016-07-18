package com.example.spring.boot.service.impl;

import com.example.spring.boot.service.CaptchaService;
import org.springframework.stereotype.Component;

/**
 * Created by pc on 2016/7/18.
 */
@Component
public class CaptchaServiceImpl implements CaptchaService{

    @Override
    public String createRegisterCaptcha(String username) {
        return null;
    }

    @Override
    public boolean isRegisterCaptchaAvailable(String username, String captcha) {
        return false;
    }

    @Override
    public void removeRegisterCaptcha(String username) {

    }
}
