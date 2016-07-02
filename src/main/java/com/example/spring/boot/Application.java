package com.example.spring.boot;

import com.example.spring.boot.config.DaoConfig;
import com.example.spring.boot.config.DataSourceConfig;
import com.example.spring.boot.config.MvcConfig;
import com.example.spring.boot.config.ServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@EnableAutoConfiguration
@ComponentScan
public class Application{
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
