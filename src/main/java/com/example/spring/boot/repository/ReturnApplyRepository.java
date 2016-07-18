package com.example.spring.boot.repository;

import com.example.spring.boot.domain.ReturnApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by fangtao on 16/7/17.
 */
public interface ReturnApplyRepository extends JpaRepository<ReturnApply, Long>, QueryDslPredicateExecutor<ReturnApply> {
}
