package com.example.spring.boot.controller;

import com.example.spring.boot.domain.ActualOrder;
import com.example.spring.boot.domain.Item;
import com.example.spring.boot.domain.Product;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.service.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MeController {
    @Resource
    private OrderService orderService;
    @Resource
    private UserService userService;
    @Resource
    private ProductService productService;
    @RequestMapping("/me")
    public User me(User user) {
        return user;
    }
    Product  product1;
    Product product2;

    @PostConstruct
    public void setUp() throws RegisterException {
        product1= new Product();
        product1.setName("spaghetti");
        product1.setPrice(2.9);
        product1.setStock(20);
        productService.save(product1);
        product2 = new Product();
        product2.setName("noodle");
        product2.setPrice(0.9);
        product2.setStock(20);
        productService.save(product2);
        User user = userService.register("username", "password");
    }

    @RequestMapping("/orders")
    public Object placeOrder() throws CreateOrderException, RegisterException {
        Product product1= new Product();
        product1.setName("spaghetti");
        product1.setPrice(2.9);
        product1.setStock(20);
        product1.setId(1L);

        Product product2 = new Product();
        product2.setName("noodle");
        product2.setPrice(0.9);
        product2.setStock(20);
        product2.setId(2L);

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setProduct(this.product1);
        item.setCount(2);
        items.add(item);

        Item item2 = new Item();
        item2.setProduct(this.product2);
        item2.setCount(3);
        items.add(item2);

        User user = new User();
        user.setId(1L);
        ActualOrder order = orderService.createOrder(user, items);
        OrderSummary o = OrderSummary.create(order);
        return o;
    }
}
