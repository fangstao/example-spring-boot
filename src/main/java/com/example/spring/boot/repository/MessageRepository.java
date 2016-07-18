package com.example.spring.boot.repository;

import com.example.spring.boot.domain.CaptchaMessage;
import com.example.spring.boot.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by pc on 2016/7/2.
 */
public interface MessageRepository extends JpaRepository<CaptchaMessage, Long>, QueryDslPredicateExecutor<CaptchaMessage> {

}
