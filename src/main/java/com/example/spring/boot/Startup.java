package com.example.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class Startup {
    public static final Logger L = LoggerFactory.getLogger(Startup.class);

    public static void main(String[] args) {

        SpringApplication.run(Startup.class);
    }
}
