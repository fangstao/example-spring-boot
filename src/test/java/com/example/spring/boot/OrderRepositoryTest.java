package com.example.spring.boot;

import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.OrderState;
import com.example.spring.boot.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@DataJpaTest
@RunWith(SpringRunner.class)
public class OrderRepositoryTest {
    @Resource
    private OrderRepository orderRepository;

    @Test
    public void saveOrder() throws Exception {
        Order order = new Order();
        order.setOrderState(OrderState.WAIT_PAYMENT);
        orderRepository.save(order);

    }
}
