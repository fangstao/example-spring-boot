package com.example.spring.boot;


import com.example.spring.boot.domain.*;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.repository.RefundApplyRepository;
import com.example.spring.boot.service.RefundApplyService;
import com.example.spring.boot.service.impl.OrderServiceImpl;
import com.example.spring.boot.service.impl.RefundApplyServiceImpl;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderRefundTest {
    private Long orderId = 1L;
    private Long orderIdWithWaitPaymentState = 2L;
    private Long orderIdExistsWaitAgreeApply = 3L;
    private Long orderIdWithRefusedApply = 4L;

    private Long generatedApplyId = 1L;
    private Long refundId = 1L;
    private Long refuseRefundId = 2L;
    private Long refuseRefundIdWithInvalidState = 3L;
    private Long cancelRefundId = 4L;
    private Long cancelRefundIdWithInvalidState = 5L;
    private Long orderIdWithReturnApplyInProcess = 6L;

    private String refuseRemark = "特殊商品不支持退款";
    private RefundReason refundReason = RefundReason.INAPPROPRIATE;
    private String refundRemark = "产品不合适";
    private OrderServiceImpl orderService;
    private OrderRepository orderRepository;
    private RefundApplyRepository refundApplyRepository;
    private RefundApplyServiceImpl refundApplyService;

    @Before
    public void setUp() throws Exception {
        orderService = new OrderServiceImpl();

        orderRepository = mockOrderRepository();
        orderService.setOrderRepository(orderRepository);

        refundApplyRepository = mockRefundApplyRepository();
        orderService.setRefundApplyRepository(refundApplyRepository);

        refundApplyService = new RefundApplyServiceImpl();

        refundApplyService.setRefundApplyRepository(refundApplyRepository);
    }

    private RefundApplyRepository mockRefundApplyRepository() {
        RefundApplyRepository refundApplyRepository = mock(RefundApplyRepository.class);
        when(refundApplyRepository.save(any(RefundApply.class))).then(new Answer<RefundApply>() {
            @Override
            public RefundApply answer(InvocationOnMock invocation) throws Throwable {
                RefundApply apply = invocation.getArgumentAt(0, RefundApply.class);
                apply.setId(generatedApplyId);
                return apply;
            }
        });

        when(refundApplyRepository.findOne(refundId)).then(new Answer<RefundApply>() {
            @Override
            public RefundApply answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderId);
                order.setState(OrderState.WAIT_CLAIM);

                RefundApply apply = new RefundApply();
                apply.setId(refundId);
                apply.setState(RefundState.WAIT_AGREE);
                apply.setReason(refundReason);
                apply.setRemark(refundRemark);
                apply.setOrder(order);
                return apply;
            }
        });
        when(refundApplyRepository.findOne(refuseRefundId)).then(new Answer<RefundApply>() {
            @Override
            public RefundApply answer(InvocationOnMock invocation) throws Throwable {
                RefundApply apply = new RefundApply();
                apply.setId(refuseRefundId);
                apply.setState(RefundState.WAIT_AGREE);
                apply.setReason(refundReason);
                apply.setRemark(refundRemark);
                return apply;
            }
        });
        when(refundApplyRepository.findOne(refuseRefundIdWithInvalidState)).then(new Answer<RefundApply>() {
            @Override
            public RefundApply answer(InvocationOnMock invocation) throws Throwable {
                RefundApply apply = new RefundApply();
                apply.setId(refuseRefundIdWithInvalidState);
                apply.setState(RefundState.REFUSED);
                apply.setReason(refundReason);
                apply.setRemark(refundRemark);
                return apply;
            }
        });
        when(refundApplyRepository.findOne(cancelRefundId)).then(new Answer<RefundApply>() {
            @Override
            public RefundApply answer(InvocationOnMock invocation) throws Throwable {
                RefundApply apply = new RefundApply();
                apply.setId(cancelRefundId);
                apply.setState(RefundState.WAIT_AGREE);
                apply.setRemark(refundRemark);
                apply.setReason(refundReason);
                return apply;
            }
        });
        when(refundApplyRepository.findOne(cancelRefundIdWithInvalidState)).then(new Answer<RefundApply>() {
            @Override
            public RefundApply answer(InvocationOnMock invocation) throws Throwable {
                RefundApply apply = new RefundApply();
                apply.setId(cancelRefundIdWithInvalidState);
                apply.setState(RefundState.CANCELED);
                apply.setRemark(refundRemark);
                apply.setReason(refundReason);
                return apply;
            }
        });
        return refundApplyRepository;
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
        });
        when(orderRepository.findOne(orderIdWithWaitPaymentState)).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderIdWithWaitPaymentState);
                order.setState(OrderState.WAIT_PAYMENT);
                return order;
            }
        });
        when(orderRepository.findOne(orderIdExistsWaitAgreeApply)).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderIdWithWaitPaymentState);
                order.setState(OrderState.WAIT_PAYMENT);
                RefundApply apply = new RefundApply();
                apply.setState(RefundState.WAIT_AGREE);
                apply.setOrder(order);
                order.setRefundApplies(Lists.newArrayList(apply));
                return order;
            }
        });
        when(orderRepository.findOne(orderIdWithReturnApplyInProcess)).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderIdWithReturnApplyInProcess);
                order.setState(OrderState.WAIT_PAYMENT);

                ReturnApply apply = new ReturnApply();
                apply.setState(ReturnState.WAIT_AGREE);
                apply.setId(refundId);

                order.setReturnApplies(Lists.newArrayList(apply));
                return order;
            }
        });

        when(orderRepository.findOne(orderIdWithRefusedApply)).then(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order order = new Order();
                order.setId(orderIdWithRefusedApply);
                order.setState(OrderState.WAIT_CLAIM);
                RefundApply apply = new RefundApply();
                apply.setState(RefundState.REFUSED);
                apply.setOrder(order);
                order.setRefundApplies(Lists.newArrayList(apply));
                return order;
            }
        });
        return orderRepository;
    }

    @Test
    public void applyRefundOnce() throws Exception {
        RefundApply apply = orderService.applyRefund(orderId, refundReason, refundRemark);

        assertThat(apply.getId()).isNotNull();
        assertThat(apply.getReason()).isEqualTo(RefundReason.INAPPROPRIATE);
        assertThat(apply.getRemark()).isEqualTo(refundRemark);
    }

    @Test
    public void applyRefundTwice() throws Exception {
        RefundApply apply = orderService.applyRefund(orderIdWithRefusedApply, refundReason, refundRemark);

        assertThat(apply.getId()).isNotNull();
        assertThat(apply.getReason()).isEqualTo(RefundReason.INAPPROPRIATE);
        assertThat(apply.getRemark()).isEqualTo(refundRemark);
    }

    @Test(expected = IllegalStateException.class)
    public void existsRefundApplyInProcess() throws Exception {
        orderService.applyRefund(orderIdExistsWaitAgreeApply, refundReason, refundRemark);
    }

    @Test(expected = IllegalStateException.class)
    public void existsReturnApplyInProcess() throws Exception {
        orderService.applyRefund(orderIdWithReturnApplyInProcess, refundReason, refundRemark);
    }


    @Test(expected = IllegalStateException.class)
    public void applyRefundWithWaitPaymentState() throws Exception {
        orderService.applyRefund(orderIdWithWaitPaymentState, refundReason, refundRemark);
    }

    @Test
    public void agreeRefund() throws Exception {
        RefundApply apply = refundApplyService.agree(refundId);

        assertThat(apply.getState()).isEqualTo(RefundState.AGREED);

        assertThat(apply.getOrder().getState()).isEqualTo(OrderState.CANCELED);
    }

    @Test
    public void refuseRefund() throws Exception {
        RefundApply apply = refundApplyService.refuse(refuseRefundId, refuseRemark);
        assertThat(apply.getState()).isEqualTo(RefundState.REFUSED);

    }

    @Test(expected = IllegalStateException.class)
    public void refuseRefundWithInvalidState() throws Exception {
        refundApplyService.refuse(refuseRefundIdWithInvalidState, refuseRemark);
    }

    @Test
    public void cancelRefundApply() throws Exception {
        RefundApply apply = refundApplyService.cancel(cancelRefundId);
        assertThat(apply.getState()).isEqualTo(RefundState.CANCELED);

    }

    @Test(expected = IllegalStateException.class)
    public void cancelRefundApplyWithInvalidState() throws Exception {
        RefundApply apply = refundApplyService.cancel(cancelRefundIdWithInvalidState);

    }

}
