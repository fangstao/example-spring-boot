package com.example.spring.boot.repository;

import com.example.spring.boot.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by fangtao on 16/7/17.
 */
public interface CommentRepository extends JpaRepository<Comment,Long>, QueryDslPredicateExecutor<Comment>{
}
