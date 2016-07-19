package com.example.spring.boot.repository;

import com.example.spring.boot.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by pc on 2016/7/19.
 */
public interface ProductRepository extends JpaRepository<Product, Long>, QueryDslPredicateExecutor<Product> {
}
