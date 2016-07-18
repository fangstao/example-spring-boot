package com.example.spring.boot;


import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.OrderState;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderClaimServiceTest {
    private OrderServiceImpl orderService;
    private OrderRepository orderRepository;
    private Long orderId = 1L;
    private Long orderIdWithInvalidState = 2L;

    @Before
    public void setUp() throws Exception {
        orderService = new OrderServiceImpl();
        orderRepository = mockOrderRepository();
        orderService.setOrderRepository(orderRepository);
    }

    @Test
    public void claimOrder() throws Exception {
        Order order = orderService.claim(orderId);
        assertThat(order.getState()).isEqualTo(OrderState.WAIT_COMMENT);
    }

    @Test(expected = IllegalStateException.class)
    public void claimOrderWithInvalidState() throws Exception {
        orderService.claim(orderIdWithInvalidState);

    }

    private OrderRepository mockOrderRepository() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findOne(orderId)).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderId);
                order.setState(OrderState.WAIT_CLAIM);
                return order;
            }
        }).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderId);
                order.setState(OrderState.WAIT_COMMENT);
                return order;
            }
        });

        when(orderRepository.findOne(orderIdWithInvalidState)).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderIdWithInvalidState);
                order.setState(OrderState.WAIT_COMMENT);
                return order;
            }
        });
        return orderRepository;
    }
}
