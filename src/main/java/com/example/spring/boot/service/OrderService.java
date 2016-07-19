package com.example.spring.boot.service;


import com.example.spring.boot.domain.*;

import java.util.List;

public interface OrderService {
    ActualOrder createOrder(User user, List<Item> items) throws CreateOrderException;

    ActualOrder findById(Long id);

    ActualOrder pay(Long orderId);

    ActualOrder claim(long orderId);

    RefundApply applyRefund(Long orderId, RefundReason refundReason, String refundRemark);

    Comment comment(Long orderId, CommentGrade positive, String commentRemark, int score);

    Shipment ship(long orderId, String shipmentCompany, String shipmentSerial);

    ReturnApply applyReturn(Long orderId, ReturnReason reason, String remark);

}
