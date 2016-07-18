package com.example.spring.boot.config;

import com.example.spring.boot.request.resolver.PrincipalHandlerMethodArgumentsResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Resource
    private MessageSource messageSource;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PrincipalHandlerMethodArgumentsResolver());
    }




    @Override
    public Validator getValidator() {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.validator.fail_fast", "true");
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationPropertyMap(properties);
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }
}
