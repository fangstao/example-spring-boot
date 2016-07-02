package com.example.spring.boot.domain;

/**
 * Created by pc on 2016/7/2.
 */
public class CaptchaMessage extends Message{

    private String fromIp;

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

}
