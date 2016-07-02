package com.example.spring.boot.controller;

import com.example.spring.boot.domain.User;
import com.example.spring.boot.service.CaptchaService;
import com.example.spring.boot.service.UserService;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.*;

/**
 * Created by pc on 2016/7/2.
 */
public class RegisterController {
    private UserService userService;

    private CaptchaService captchaService;


    public ResponseEntity register(RegisterParam param) {
        if (isUserRegisted(param.getUsername())) {
            return badRequest().body("username.exists");
        }
        boolean available = captchaService.isRegisterCaptchaAvailable(param.getUsername(), param.getCaptcha());
        if (!available) {
            return badRequest().body("captcha.invalid");
        }
        userService.register(param.getUsername(), param.getPassword());
        captchaService.removeRegisterCaptcha(param.getUsername());
        return ok("register.success");
    }

    private boolean isUserRegisted(String username) {
        User user = userService.findByUsername(username);
        return user != null;
    }


    public ResponseEntity getRegisterCaptcha(String username) {
        if(isUserRegisted(username)){
            return badRequest().body("username.exists");
        }
        String captcha = captchaService.createRegisterCaptcha(username);
        return ok("captcha.generated");
    }

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Resource
    public void setCaptchaService(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }
}
