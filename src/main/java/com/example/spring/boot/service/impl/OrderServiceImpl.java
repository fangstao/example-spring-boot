package com.example.spring.boot.service.impl;

import com.example.spring.boot.domain.Item;
import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.repository.ItemRepository;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.service.CreateOrderException;
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

    private ItemRepository itemRepository;

    @Override
    public Order createOrder(User user, List<Item> items) throws CreateOrderException {
        Order order = Order.create(user, items);
        orderRepository.save(order);
        saveOrderItems(items, order);
        return order;
    }

    private void saveOrderItems(List<Item> items, Order order) {
        items.stream()
                .forEach(item -> {
                    item.setOrder(order);
                    itemRepository.save(item);
                });
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findOne(id);
    }

    @Override
    public void pay(Long orderId) {
        Order order = findById(orderId);
        order.pay();
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


    @Resource
    public void setItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
}
