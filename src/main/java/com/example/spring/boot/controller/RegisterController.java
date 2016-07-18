package com.example.spring.boot.controller;

import com.example.spring.boot.domain.CaptchaMessage;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class RegisterController {
    private UserService userService;

    private CaptchaService captchaService;

    private MessageService messageService;


    @PostMapping("/register")
    public ResponseEntity register(RegisterParam param) {
        boolean available = captchaService.isRegisterCaptchaAvailable(param.getUsername(), param.getCaptcha());
        if (!available) {
            return badRequest().body("captcha.invalid");
        }
        try {
            userService.register(param.getUsername(), param.getPassword());
            captchaService.removeRegisterCaptcha(param.getUsername());
            return ok("register.success");
        } catch (UserHasBeenRegisteredException e) {
            return badRequest().body("username.exists");
        } catch (RegisterException e) {
            return badRequest().body("register.failure");
        }

    }

    private boolean isUserRegistered(String username) {
        User user = userService.findByUsername(username);
        return user != null;
    }


    @GetMapping("/register/captcha")
    public ResponseEntity getRegisterCaptcha(String username, String ip) {
        if (isUserRegistered(username)) {
            return badRequest().body("username.exists");
        }
        String captcha = captchaService.createRegisterCaptcha(username);

        String content = createMessageContent(username, captcha);

        CaptchaMessage message = CaptchaMessage.create(username, content, ip);

        try {
            messageService.send(message);
            return ok("captcha.generated");
        } catch (SendMessageWithinIntervalException e) {
            return badRequest().body("captcha.within.interval");
        } catch (SendMessageTooManyTimesException e) {
            return badRequest().body("captcha.too.many");
        } catch (MessageException e) {
            return badRequest().body("captcha.get.failure");
        }
    }


    private String createMessageContent(String username, String captcha) {
        return null;
    }

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Resource
    public void setCaptchaService(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @Resource
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
