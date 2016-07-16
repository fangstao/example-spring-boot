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
import static org.mockito.Mockito.*;


public class OrderPaymentServiceTest {
    private OrderServiceImpl orderService;
    private OrderRepository orderRepository;
    private Long orderId = 1L;
    private Long orderIdWithInvalidState = 2L;

    @Before
    public void setUp() throws Exception {
        orderService = new OrderServiceImpl();
        orderRepository = createOrderRepository();
        orderService.setOrderRepository(orderRepository);
    }

    private OrderRepository createOrderRepository() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findOne(orderId)).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderId);
                order.setState(OrderState.WAIT_PAYMENT);
                return order;
            }
        }).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderId);
                order.setState(OrderState.WAIT_SHIPMENT);
                return order;
            }
        });

        when(orderRepository.findOne(orderIdWithInvalidState)).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderId);
                order.setState(OrderState.WAIT_SHIPMENT);
                return order;
            }
        });
        return orderRepository;
    }

    @Test
    public void paySuccess() throws Exception {
        orderService.pay(orderId);
        Order order = orderService.findById(orderId);
        assertThat(order.getState()).isEqualTo(OrderState.WAIT_SHIPMENT);
    }

    @Test(expected = IllegalStateException.class)
    public void payWithInvalidState() throws Exception {
        orderService.pay(orderIdWithInvalidState);
    }
}
