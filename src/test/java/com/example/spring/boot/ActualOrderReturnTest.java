package com.example.spring.boot;

import com.example.spring.boot.domain.*;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.repository.ReturnApplyRepository;
import com.example.spring.boot.repository.ReturnShipmentRepository;
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
public class ActualOrderReturnTest {
    OrderServiceImpl orderService;
    OrderRepository orderRepository;
    ReturnApplyRepository returnApplyRepository;
    ReturnShipmentRepository returnShipmentRepository;
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
    private Long shipReturnApplyId = 6L;
    private Long shipReturnApplyIdWithDuplicateShipment = 7L;
    private Long claimReturnApplyId = 8L;
    private Long claimReturnApplyIdWithInvalidState = 9L;
    private String refuseRemark = "don't support return.";
    private String shipReturnCompany = "sf-express";
    private String shipReturnSerial = "13865882466";
    private Long generatedShipmentId = 1L;


    @Before
    public void setUp() throws Exception {
        orderService = new OrderServiceImpl();
        orderRepository = mockOrderRepository();
        orderService.setOrderRepository(orderRepository);
        returnApplyRepository = mockReturnApplyRepository();
        orderService.setReturnApplyRepository(returnApplyRepository);

        returnApplyService = new ReturnApplyServiceImpl();
        returnApplyService.setReturnApplyRepository(returnApplyRepository);

        returnShipmentRepository = mockReturnShipmentRepository();
        returnApplyService.setReturnShipmentRepository(returnShipmentRepository);
    }

    private ReturnShipmentRepository mockReturnShipmentRepository() {
        ReturnShipmentRepository returnShipmentRepository = mock(ReturnShipmentRepository.class);
        when(returnShipmentRepository.save(any(ReturnShipment.class))).then(new Answer<ReturnShipment>() {
            @Override
            public ReturnShipment answer(InvocationOnMock invocation) throws Throwable {
                ReturnShipment shipment = invocation.getArgumentAt(0, ReturnShipment.class);
                shipment.setId(generatedShipmentId);
                return shipment;
            }
        });
        return returnShipmentRepository;

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
        when(returnApplyRepository.findOne(shipReturnApplyId)).thenAnswer(new Answer<ReturnApply>() {
            @Override
            public ReturnApply answer(InvocationOnMock invocation) throws Throwable {
                ReturnApply apply = new ReturnApply();
                apply.setState(ReturnState.AGREED);
                apply.setId(shipReturnApplyId);
                return apply;
            }
        });
        when(returnApplyRepository.findOne(claimReturnApplyId)).thenAnswer(new Answer<ReturnApply>() {
            @Override
            public ReturnApply answer(InvocationOnMock invocation) throws Throwable {
                ReturnApply apply = new ReturnApply();
                apply.setState(ReturnState.WAIT_CLAIM);
                apply.setId(claimReturnApplyId);
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setState(ActualOrderState.SUCCESS);
                apply.setOrder(actualOrder);
                return apply;
            }
        });
        when(returnApplyRepository.findOne(claimReturnApplyIdWithInvalidState)).thenAnswer(new Answer<ReturnApply>() {
            @Override
            public ReturnApply answer(InvocationOnMock invocation) throws Throwable {
                ReturnApply apply = new ReturnApply();
                apply.setState(ReturnState.CLAIMED);
                apply.setId(claimReturnApplyIdWithInvalidState);
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setState(ActualOrderState.CANCELED);
                apply.setOrder(actualOrder);
                return apply;
            }
        });
        return returnApplyRepository;
    }

    private OrderRepository mockOrderRepository() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findOne(orderId)).then(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderId);
                actualOrder.setState(ActualOrderState.SUCCESS);
                return actualOrder;
            }
        });
        when(orderRepository.findOne(orderIdWithIllegalState)).thenAnswer(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderIdWithIllegalState);
                actualOrder.setTotalPrice(213.0);
                actualOrder.setState(ActualOrderState.WAIT_SHIPMENT);
                return actualOrder;
            }
        });
        when(orderRepository.findOne(orderIdWithApplyInProcess)).thenAnswer(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                RefundApply refundApply = new RefundApply();
                refundApply.setState(RefundState.WAIT_AGREE);
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderIdWithApplyInProcess);
                actualOrder.setState(ActualOrderState.SUCCESS);
                actualOrder.setRefundApplies(Lists.newArrayList(refundApply));
                return actualOrder;
            }
        });
        when(orderRepository.findOne(orderIdWithRefundApplyInProcess)).thenAnswer(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                RefundApply refundApply = new RefundApply();
                refundApply.setState(RefundState.WAIT_AGREE);
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderIdWithRefundApplyInProcess);
                actualOrder.setTotalPrice(213.0);
                actualOrder.setState(ActualOrderState.SUCCESS);
                actualOrder.setRefundApplies(Lists.newArrayList(refundApply));
                return actualOrder;
            }
        });
        return orderRepository;
    }

    /**
     * 申请退货成功
     * @throws Exception
     */
    @Test
    public void applyReturn() throws Exception {
        ReturnApply apply = orderService.applyReturn(orderId, ReturnReason.INAPPROPRIATE, returnRemark);
        assertThat(apply.getReason()).isEqualTo(ReturnReason.INAPPROPRIATE);
        assertThat(apply.getRemark()).isEqualTo(returnRemark);
        assertThat(apply.getState()).isEqualTo(ReturnState.WAIT_AGREE);
    }

    /**
     * 订单状态不正确，事情退货失败
     * @throws Exception
     */
    @Test(expected  = IllegalStateException.class)
    public void applyReturnWithIllegalState() throws Exception {
        orderService.applyReturn(orderIdWithIllegalState, ReturnReason.INAPPROPRIATE, returnRemark);
    }

    /**
     * 已存在处理中的退货，申请退货失败
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void applyReturnWithReturnApplyInProcess() throws Exception {
        orderService.applyReturn(orderIdWithApplyInProcess, ReturnReason.INAPPROPRIATE, returnRemark);
    }

    /**
     * 已存在处理中的退款申请，申请失败
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void applyReturnWithRefundApplyInProcess() throws Exception {
        orderService.applyReturn(orderIdWithRefundApplyInProcess, ReturnReason.INAPPROPRIATE, returnRemark);
    }

    /**
     * 卖家同意退货
     * @throws Exception
     */
    @Test
    public void agreeReturnApply() throws Exception {
        ReturnApply apply = returnApplyService.agree(returnApplyId);
        assertThat(apply.getState()).isEqualTo(ReturnState.AGREED);
    }

    /**
     * 同意退货失败，订单状态不正确
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void agreeReturnApplyWithIllegalState() throws Exception {
        ReturnApply apply = returnApplyService.agree(returnApplyIdWithIllegalState);
    }

    /**
     * 卖家拒绝退货申请
     * @throws Exception
     */
    @Test
    public void refuseReturnApplySuccess() throws Exception {
        ReturnApply apply = returnApplyService.refuse(refuseReturnApplyId, refuseRemark);
        assertThat(apply.getState()).isEqualTo(ReturnState.REFUSED);
        assertThat(apply.getRefuseRemark()).isEqualTo(refuseRemark);

    }

    /**
     * 订单状态不正确，拒绝退货失败
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void refuseReturnApplyWithIllegalState() throws Exception {
        ReturnApply apply = returnApplyService.refuse(refuseReturnApplyIdWithIllegalState, refuseRemark);

    }

    /**
     * 买家发货
     * @throws Exception
     */
    @Test
    public void shipGoodsToSeller() throws Exception {
        ReturnShipment shipment = returnApplyService.ship(shipReturnApplyId, shipReturnCompany, shipReturnSerial);
        assertThat(shipment.getCompany()).isEqualToIgnoringCase(shipReturnCompany);
        assertThat(shipment.getSerial()).isEqualTo(shipReturnSerial);
        assertThat(shipment.getApply().getState()).isEqualTo(ReturnState.WAIT_CLAIM);
    }

    /**
     * 卖家签收货物
     * @throws Exception
     */
    @Test
    public void claimReturnShipment() throws Exception {
        ReturnApply apply = returnApplyService.claim(claimReturnApplyId);
        assertThat(apply.getState()).isEqualTo(ReturnState.CLAIMED);
        assertThat(((ActualOrder)apply.getOrder()).getState()).isEqualTo(ActualOrderState.CANCELED);
    }

    /**
     * 订单状态不正确，签收失败
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void claimReturnShipmentWithInvalidState() throws Exception {
        returnApplyService.claim(claimReturnApplyIdWithInvalidState);
    }
}
