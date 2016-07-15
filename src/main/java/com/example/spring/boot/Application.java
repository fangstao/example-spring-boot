package com.example.spring.boot;

import com.example.spring.boot.config.Events;
import com.example.spring.boot.config.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.statemachine.StateMachine;

import javax.annotation.Resource;

@SpringBootApplication
public class Application{

    public static void main(String[] args) {
        SpringApplication.run(Application.class);

    }

}
