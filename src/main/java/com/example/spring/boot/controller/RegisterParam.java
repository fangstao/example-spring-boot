package com.example.spring.boot.controller;

/**
 * Created by pc on 2016/7/2.
 */
public class RegisterParam {
    private String username;
    private String password;
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RegisterParam() {
    }

    private RegisterParam(String username, String password, String captcha) {
        this.captcha = captcha;
        this.password = password;
        this.username = username;
    }

    public static final RegisterParam create(String username, String password, String captcha) {
        return new RegisterParam(username, password, captcha);
    }
}
