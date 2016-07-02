package com.example.spring.boot;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Created by pc on 2016/6/28.
 */
public class HomeController {
    public String home() {
        ArrayList<String> lists = Lists.newArrayList();
        return "hello world";
    }
}
