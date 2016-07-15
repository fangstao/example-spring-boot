package com.example.spring.boot.repository;


import com.example.spring.boot.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface ItemRepository extends JpaRepository<Item, Long>, QueryDslPredicateExecutor<Item> {

}
