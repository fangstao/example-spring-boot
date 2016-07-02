package com.example.spring.boot;


import com.example.spring.boot.controller.RegisterController;
import com.example.spring.boot.controller.RegisterParam;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.service.CaptchaService;
import com.example.spring.boot.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

//@ContextConfiguration
//@RunWith(SpringJUnit4ClassRunner.class)
public class RegisterControllerTest {
    String username = "tommy";
    String password = "tommy123";
    String captcha = "12345";
    String registedUsername = "jack";
    String invalidCaptcha = "67890";
    RegisterController controller;
    UserService userService;
    CaptchaService captchaService;

    @Before
    public void setUp() throws Exception {
        controller = new RegisterController();
        userService = createUserService();
        captchaService = createCaptchaService();
        controller.setUserService(userService);
        controller.setCaptchaService(captchaService);
    }

    private CaptchaService createCaptchaService() {
        CaptchaService captchaService = mock(CaptchaService.class);
        when(captchaService.createRegisterCaptcha(username)).thenReturn(captcha);
        when(captchaService.isRegisterCaptchaAvailable(username, captcha)).thenReturn(true);
        when(captchaService.isRegisterCaptchaAvailable(username, invalidCaptcha)).thenReturn(false);
        return captchaService;
    }

    private UserService createUserService() {
        UserService userService = mock(UserService.class);
        when(userService.findByUsername(registedUsername)).thenReturn(new User());
        when(userService.findByUsername(username)).thenReturn(null);
        return userService;
    }

    @Test
    public void registerSuccess() throws Exception {
        ResponseEntity response = controller.register(RegisterParam.create(username, password, captcha));
        assertEquals(200, response.getStatusCode().value());
        assertEquals("register.success", response.getBody());
    }

    @Test
    public void usernameHasRegisted() throws Exception {
        ResponseEntity response = controller.register(RegisterParam.create(registedUsername, password, captcha));
        assertEquals(400, response.getStatusCode().value());
        assertEquals("username.exists", response.getBody());
    }

    @Test
    public void registerCaptchaInvalid() throws Exception {
        ResponseEntity response = controller.register(RegisterParam.create(username, password, invalidCaptcha));
        assertEquals(400, response.getStatusCode().value());
        assertEquals("captcha.invalid", response.getBody());
    }

    @Test
    public void requestCaptchaForUsernameSuccess() throws Exception {
        ResponseEntity response = controller.getRegisterCaptcha(username);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void requestCaptchaForRegistedUsernameFailure() throws Exception {
        ResponseEntity response = controller.getRegisterCaptcha(registedUsername);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("username.exists", response.getBody());
    }
}
