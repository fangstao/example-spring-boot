package com.example.spring.boot.repository;

import com.example.spring.boot.domain.ReturnShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by pc on 2016/7/18.
 */
public interface ReturnShipmentRepository extends JpaRepository<ReturnShipment, Long>, QueryDslPredicateExecutor<ReturnShipment> {
}
