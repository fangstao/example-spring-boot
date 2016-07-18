package com.example.spring.boot.repository;


import com.example.spring.boot.domain.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface ShipmentRepository extends JpaRepository<Shipment, Long>, QueryDslPredicateExecutor<Shipment> {
}
