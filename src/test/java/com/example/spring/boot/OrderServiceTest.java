package com.example.spring.boot;

import com.example.spring.boot.domain.Item;
import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.Product;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.service.OrderService;
import com.example.spring.boot.service.impl.OrderServiceImpl;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.hamcrest.core.IsAnything;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class OrderServiceTest {
    private OrderService orderService;

    @Before
    public void setUp() throws Exception {
        orderService = new OrderServiceImpl();

    }

    @Test
    public void createOrderSuccess() throws Exception {
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);

        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setProduct(product);
        item.setCount(2);
        items.add(item);

        Item item2 = new Item();
        item.setProduct(product2);
        item.setCount(3);
        items.add(item2);
        Order order = orderService.createOrder(user, items);
        assertEquals(user, order.getUser());

    }
}
