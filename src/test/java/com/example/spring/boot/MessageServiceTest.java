package com.example.spring.boot;

import com.example.spring.boot.service.MessageService;
import com.example.spring.boot.domain.CaptchaMessage;
import com.example.spring.boot.service.MessageServiceImpl;
import com.example.spring.boot.service.SendMessageWithinInterval;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by pc on 2016/7/2.
 */
public class MessageServiceTest {

    MessageService messageService;
    CaptchaMessage message;
    @Before
    public void setUp() throws Exception {
        messageService = new MessageServiceImpl();
        message = new CaptchaMessage();
        message.setContent("your register captcha is 123456.");
        message.setFromIp("10.1.2.33");
        message.setSignature("microsoft");

    }


    @Test
    public void sendCaptchaMessage() throws Exception {
        message.setPhone("15800826960");
        messageService.send(message);
    }

    @Test(expected = SendMessageWithinInterval.class)
    public void sendCaptchaMessageWithinMinIntervalException() throws Exception {
        message.setPhone("15800826961");
        messageService.send(message);
    }
}
