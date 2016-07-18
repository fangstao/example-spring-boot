package com.example.spring.boot;

import com.example.spring.boot.domain.*;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.repository.ReturnApplyRepository;
import com.example.spring.boot.service.RefundApplyService;
import com.example.spring.boot.service.impl.OrderServiceImpl;
import com.example.spring.boot.service.impl.ReturnApplyServiceImpl;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by fangtao on 16/7/17.
 */
public class OrderReturnTest {
    OrderServiceImpl orderService;
    OrderRepository orderRepository;
    ReturnApplyRepository returnApplyRepository;

    private ReturnApplyServiceImpl returnApplyService;

    private Long orderId = 1L;
    private String returnRemark = "I don't like this color.";
    private Long generatedApplyId = 1L;
    private Long orderIdWithIllegalState = 2L;
    private Long orderIdWithApplyInProcess = 3L;
    private Long orderIdWithRefundApplyInProcess = 4L;
    private Long returnApplyId = 2L;
    private Long returnApplyIdWithIllegalState = 3L;
    private Long refuseReturnApplyId = 4L;
    private Long refuseReturnApplyIdWithIllegalState = 5L;
    private String refuseRemark = "don't support return.";


    @Before
    public void setUp() throws Exception {
        orderService = new OrderServiceImpl();
        orderRepository = mockOrderRepository();
        orderService.setOrderRepository(orderRepository);
        returnApplyRepository = mockReturnApplyRepository();
        orderService.setReturnApplyRepository(returnApplyRepository);

        returnApplyService = new ReturnApplyServiceImpl();
        returnApplyService.setReturnApplyRepository(returnApplyRepository);
    }

    private ReturnApplyRepository mockReturnApplyRepository() {
        ReturnApplyRepository returnApplyRepository = mock(ReturnApplyRepository.class);
        when(returnApplyRepository.save(any(ReturnApply.class))).then(new Answer<ReturnApply>() {
            @Override
            public ReturnApply answer(InvocationOnMock invocation) throws Throwable {
                ReturnApply apply = invocation.getArgumentAt(0, ReturnApply.class);
                apply.setId(generatedApplyId);
                return apply;
            }
        });

        when(returnApplyRepository.findOne(returnApplyId)).then(new Answer<ReturnApply>() {
            @Override
            public ReturnApply answer(InvocationOnMock invocation) throws Throwable {
                ReturnApply apply = new ReturnApply();
                apply.setId(returnApplyId);
                apply.setState(ReturnState.WAIT_AGREE);
                return apply;
            }
        });
        when(returnApplyRepository.findOne(returnApplyIdWithIllegalState)).then(new Answer<ReturnApply>() {
            @Override
            public ReturnApply answer(InvocationOnMock invocation) throws Throwable {
                ReturnApply apply = new ReturnApply();
                apply.setId(returnApplyIdWithIllegalState);
                apply.setState(ReturnState.AGREED);
                return apply;
            }
        });
        when(returnApplyRepository.findOne(refuseReturnApplyIdWithIllegalState)).thenAnswer(new Answer<ReturnApply>() {
            @Override
            public ReturnApply answer(InvocationOnMock invocation) throws Throwable {
                ReturnApply apply = new ReturnApply();
                apply.setId(refuseReturnApplyIdWithIllegalState);
                apply.setState(ReturnState.AGREED);
                return apply;
            }
        });
        when(returnApplyRepository.findOne(refuseReturnApplyId)).thenAnswer(new Answer<ReturnApply>() {
            @Override
            public ReturnApply answer(InvocationOnMock invocation) throws Throwable {
                ReturnApply apply = new ReturnApply();
                apply.setId(refuseReturnApplyId);
                apply.setState(ReturnState.WAIT_AGREE);
                return apply;
            }
        });
        return returnApplyRepository;
    }

    private OrderRepository mockOrderRepository() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findOne(orderId)).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderId);
                order.setState(OrderState.SUCCESS);
                return order;
            }
        });
        when(orderRepository.findOne(orderIdWithIllegalState)).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderIdWithIllegalState);
                order.setTotalPrice(213.0);
                order.setState(OrderState.WAIT_SHIPMENT);
                return order;
            }
        });
        when(orderRepository.findOne(orderIdWithApplyInProcess)).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                RefundApply refundApply = new RefundApply();
                refundApply.setState(RefundState.WAIT_AGREE);
                Order order = new Order();
                order.setId(orderIdWithApplyInProcess);
                order.setState(OrderState.SUCCESS);
                order.setRefundApplies(Lists.newArrayList(refundApply));
                return order;
            }
        });
        when(orderRepository.findOne(orderIdWithRefundApplyInProcess)).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                RefundApply refundApply = new RefundApply();
                refundApply.setState(RefundState.WAIT_AGREE);
                Order order = new Order();
                order.setId(orderIdWithRefundApplyInProcess);
                order.setTotalPrice(213.0);
                order.setState(OrderState.SUCCESS);
                order.setRefundApplies(Lists.newArrayList(refundApply));
                return order;
            }
        });
        return orderRepository;
    }

    @Test
    public void applyReturn() throws Exception {
        ReturnApply apply = orderService.applyReturn(orderId, ReturnReason.INAPPROPRIATE, returnRemark);
        assertThat(apply.getReason()).isEqualTo(ReturnReason.INAPPROPRIATE);
        assertThat(apply.getRemark()).isEqualTo(returnRemark);
        assertThat(apply.getState()).isEqualTo(ReturnState.WAIT_AGREE);
    }

    @Test(expected = IllegalStateException.class)
    public void applyReturnWithIllegalState() throws Exception {
        orderService.applyReturn(orderIdWithIllegalState, ReturnReason.INAPPROPRIATE, returnRemark);
    }

    @Test(expected = IllegalStateException.class)
    public void applyReturnWithReturnApplyInProcess() throws Exception {
        orderService.applyReturn(orderIdWithApplyInProcess, ReturnReason.INAPPROPRIATE, returnRemark);
    }

    @Test(expected = IllegalStateException.class)
    public void applyReturnWithRefundApplyInProcess() throws Exception {
        orderService.applyReturn(orderIdWithRefundApplyInProcess, ReturnReason.INAPPROPRIATE, returnRemark);
    }

    @Test
    public void agreeReturnApply() throws Exception {
        ReturnApply apply = returnApplyService.agree(returnApplyId);
        assertThat(apply.getState()).isEqualTo(ReturnState.AGREED);
    }

    @Test(expected = IllegalStateException.class)
    public void agreeReturnApplyWithIllegalState() throws Exception {
        ReturnApply apply = returnApplyService.agree(returnApplyIdWithIllegalState);
    }

    @Test
    public void refuseReturnApplySuccess() throws Exception {
        ReturnApply apply = returnApplyService.refuse(refuseReturnApplyId, refuseRemark);
        assertThat(apply.getState()).isEqualTo(ReturnState.REFUSED);
        assertThat(apply.getRefuseRemark()).isEqualTo(refuseRemark);

    }

    @Test(expected = IllegalStateException.class)
    public void refuseReturnApplyWithIllegalState() throws Exception {
        ReturnApply apply = returnApplyService.refuse(refuseReturnApplyIdWithIllegalState, refuseRemark);

    }



}
