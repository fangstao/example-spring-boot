package com.example.spring.boot.repository;


import com.example.spring.boot.domain.ActualOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;


public interface OrderRepository extends JpaRepository<ActualOrder,Long>,QueryDslPredicateExecutor<ActualOrder>{
}
