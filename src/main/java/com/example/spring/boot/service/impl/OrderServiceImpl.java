package com.example.spring.boot.service.impl;

import com.example.spring.boot.domain.Item;
import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.service.OrderService;
import com.example.spring.boot.service.ProductService;
import com.example.spring.boot.service.UserService;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.LinkedHashSet;
import java.util.List;


public class OrderServiceImpl implements OrderService {

    private UserService userService;

    private OrderRepository orderRepository;

    private ProductService productService;

    @Override
    public Order createOrder(User user, List<Item> items) {
        Order order = Order.create(user, new LinkedHashSet<>(items));
        orderRepository.save(order);
        return order;
    }

    @Resource
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Resource
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
