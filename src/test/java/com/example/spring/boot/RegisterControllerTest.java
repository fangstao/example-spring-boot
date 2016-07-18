package com.example.spring.boot;


import com.example.spring.boot.controller.RegisterController;
import com.example.spring.boot.controller.RegisterParam;
import com.example.spring.boot.domain.CaptchaMessage;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
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
    String intervalUsername = "intervalUsername";
    String password = "tommy123";
    String captcha = "12345";
    String registedUsername = "jack";
    String invalidCaptcha = "67890";
    String clientIp = "192.168.1.31";
    String ipTooManyTimes = "192.168.1.1";
    RegisterController controller;
    UserService userService;
    CaptchaService captchaService;
    MessageService messageService;

    @Before
    public void setUp() throws Exception {
        controller = new RegisterController();
        userService = createUserService();
        captchaService = createCaptchaService();
        messageService = createMessageService();
        controller.setUserService(userService);
        controller.setCaptchaService(captchaService);
        controller.setMessageService(messageService);
    }

    private MessageService createMessageService() throws MessageException {
        MessageService messageService = mock(MessageService.class);

        CaptchaMessage message = CaptchaMessage.create(intervalUsername, "message content", clientIp);
        doThrow(new SendMessageWithinIntervalException("send message in interval")).when(messageService).send(argThat(new MessageMobileEqMatcher(message)));

        CaptchaMessage message2 = CaptchaMessage.create(username, "message content", ipTooManyTimes);
        doThrow(new SendMessageTooManyTimesException(("send message too many times with same ip"))).when(messageService).send(argThat(new MessageIpEqMatcher(message2)));

        return messageService;
    }

    private CaptchaService createCaptchaService() {
        CaptchaService captchaService = mock(CaptchaService.class);
        when(captchaService.createRegisterCaptcha(username)).thenReturn(captcha);
        when(captchaService.isRegisterCaptchaAvailable(username, captcha)).thenReturn(true);
        when(captchaService.isRegisterCaptchaAvailable(username, invalidCaptcha)).thenReturn(false);
        return captchaService;
    }


    private UserService createUserService() throws RegisterException {
        UserService userService = mock(UserService.class);
        when(userService.findByUsername(username)).thenReturn(null);
        when(userService.findByUsername(registedUsername)).thenReturn(new User());
        when(userService.register(registedUsername, password)).thenThrow(new UserHasBeenRegisteredException("user has been registered"));
        return userService;
    }

    @Test
    public void registerSuccess() throws Exception {
        ResponseEntity response = controller.register(RegisterParam.create(username, password, captcha));
        assertEquals(200, response.getStatusCode().value());
        assertEquals("register.success", response.getBody());
    }

    @Test
    public void usernameHasRegistered() throws Exception {
        ResponseEntity response = controller.register(RegisterParam.create(registedUsername, password, captcha));
        assertEquals(400, response.getStatusCode().value());
        assertEquals("captcha.invalid", response.getBody());
    }

    @Test
    public void registerCaptchaInvalid() throws Exception {
        ResponseEntity response = controller.register(RegisterParam.create(username, password, invalidCaptcha));
        assertEquals(400, response.getStatusCode().value());
        assertEquals("captcha.invalid", response.getBody());
    }

    @Test
    public void requestCaptchaForUsernameSuccess() throws Exception {
        ResponseEntity response = controller.getRegisterCaptcha(username, clientIp);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void requestCaptchaForRegisteredUsernameFailure() throws Exception {
        ResponseEntity response = controller.getRegisterCaptcha(registedUsername, clientIp);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("username.exists", response.getBody());
    }


    @Test
    public void requestCaptchaInInterval() throws Exception {
        ResponseEntity response = controller.getRegisterCaptcha(intervalUsername, clientIp);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("captcha.within.interval", response.getBody());

    }

    @Test
    public void requestCaptchaTooManyTimesWithSameIpInInterval() throws Exception {
        ResponseEntity response = controller.getRegisterCaptcha(username, ipTooManyTimes);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("captcha.too.many", response.getBody());
    }

    class MessageMobileEqMatcher extends ArgumentMatcher<CaptchaMessage> {
        CaptchaMessage message;

        @Override
        public boolean matches(Object argument) {
            CaptchaMessage arg = (CaptchaMessage) argument;
            return arg != null && message.getPhone().equals(arg.getPhone());
        }

        public MessageMobileEqMatcher(CaptchaMessage message) {
            this.message = message;
        }
    }

    class MessageIpEqMatcher extends ArgumentMatcher<CaptchaMessage> {
        CaptchaMessage message;

        @Override
        public boolean matches(Object argument) {
            CaptchaMessage arg = (CaptchaMessage) argument;
            return arg != null && message.getFromIp().equals(arg.getFromIp());
        }

        public MessageIpEqMatcher(CaptchaMessage message) {
            this.message = message;
        }
    }
}
