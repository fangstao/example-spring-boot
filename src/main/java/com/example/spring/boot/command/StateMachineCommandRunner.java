package com.example.spring.boot.command;

import com.example.spring.boot.config.Events;
import com.example.spring.boot.config.States;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;

import javax.annotation.Resource;

@Configuration
public class StateMachineCommandRunner implements CommandLineRunner{

    @Resource
    private StateMachine<States, Events> stateMachine;

    @Override
    public void run(String... strings) throws Exception {
        stateMachine.sendEvent(Events.E2);
        stateMachine.sendEvent(Events.E1);
    }
}
