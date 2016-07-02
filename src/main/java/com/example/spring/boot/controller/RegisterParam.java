package com.example.spring.boot.controller;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by pc on 2016/7/1.
 */
public class RegisterParam {
    @NotBlank(message = "{error.username.blank}")
    private String username;

    @NotBlank(message = "{error.password.blank}")
    private String password;

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
}
