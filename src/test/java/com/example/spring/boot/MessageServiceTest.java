package com.example.spring.boot;

import com.example.spring.boot.message.MessageService;
import org.junit.Test;

/**
 * Created by pc on 2016/7/2.
 */
public class MessageServiceTest {
    MessageService messageService;
    @Test
    public void sendRegisterCaptchaMessage() throws Exception {
        messageService.send("message");
    }
}
