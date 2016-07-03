package com.example.spring.boot.domain;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class CaptchaMessage extends Message{

    private String fromIp;

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public static final CaptchaMessage create(String phone,String content,String ip) {
        CaptchaMessage message = new CaptchaMessage();
        message.setFromIp(ip);
        message.setContent(content);
        message.setPhone(phone);
        message.setCreationDate(new Date());
        return message;
    }
}
