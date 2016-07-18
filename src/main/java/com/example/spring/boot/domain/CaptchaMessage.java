package com.example.spring.boot.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "captcha_messages")
public class CaptchaMessage extends Message {

    private String fromIp;

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public static final CaptchaMessage create(String phone, String content, String ip) {
        CaptchaMessage message = new CaptchaMessage();
        message.setFromIp(ip);
        message.setContent(content);
        message.setPhone(phone);
        message.setCreationDate(new Date());
        return message;
    }
}
