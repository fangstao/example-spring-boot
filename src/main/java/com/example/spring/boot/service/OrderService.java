package com.example.spring.boot.service;


import com.example.spring.boot.domain.Item;
import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.User;

import java.util.ArrayList;
import java.util.List;

public interface OrderService {
    Order createOrder(User user, List<Item> items) throws CreateOrderException;

    Order findById(Long id);
}
