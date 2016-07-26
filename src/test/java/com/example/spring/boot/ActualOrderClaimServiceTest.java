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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActualOrderClaimServiceTest {
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

    /**
     * 买家签收货物成功
     * @throws Exception
     */
    @Test
    public void claimOrder() throws Exception {
        ActualOrder actualOrder = orderService.claim(orderId);
        assertThat(actualOrder.getState()).isEqualTo(ActualOrderState.WAIT_COMMENT);
    }

    /**
     * 订单状态不正确，签收失败
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void claimOrderWithInvalidState() throws Exception {
        orderService.claim(orderIdWithInvalidState);

    }

    private OrderRepository mockOrderRepository() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findOne(orderId)).thenAnswer(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderId);
                actualOrder.setState(ActualOrderState.WAIT_CLAIM);
                return actualOrder;
            }
        }).thenAnswer(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderId);
                actualOrder.setState(ActualOrderState.WAIT_COMMENT);
                return actualOrder;
            }
        });

        when(orderRepository.findOne(orderIdWithInvalidState)).thenAnswer(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderIdWithInvalidState);
                actualOrder.setState(ActualOrderState.WAIT_COMMENT);
                return actualOrder;
            }
        });
        return orderRepository;
    }
}
