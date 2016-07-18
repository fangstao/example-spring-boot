package com.example.spring.boot.repository;


import com.example.spring.boot.domain.RefundApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface RefundApplyRepository extends JpaRepository<RefundApply, Long>, QueryDslPredicateExecutor<RefundApply> {
}
