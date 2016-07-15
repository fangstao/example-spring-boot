package com.example.spring.boot.domain.state;


public interface Stateful<T> {
    T getState();
}
