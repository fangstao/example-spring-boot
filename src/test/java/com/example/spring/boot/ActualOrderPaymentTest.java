package com.example.spring.boot;

import com.example.spring.boot.domain.ActualOrder;
import com.example.spring.boot.domain.ActualOrderState;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ActualOrderPaymentTest {
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
        when(orderRepository.findOne(orderId)).then(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderId);
                actualOrder.setState(ActualOrderState.WAIT_PAYMENT);
                return actualOrder;
            }
        });

        when(orderRepository.findOne(orderIdWithInvalidState)).then(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderId);
                actualOrder.setState(ActualOrderState.WAIT_SHIPMENT);
                return actualOrder;
            }
        });
        return orderRepository;
    }

    @Test
    public void paySuccess() throws Exception {
        ActualOrder actualOrder = orderService.pay(orderId);
        assertThat(actualOrder.getState()).isEqualTo(ActualOrderState.WAIT_SHIPMENT);
    }

    @Test(expected = IllegalStateException.class)
    public void payWithInvalidState() throws Exception {
        orderService.pay(orderIdWithInvalidState);
    }
}
