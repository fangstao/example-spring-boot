package com.example.spring.boot.service.impl;

import com.example.spring.boot.domain.*;
import com.example.spring.boot.repository.*;
import com.example.spring.boot.service.CreateOrderException;
import com.example.spring.boot.service.OrderService;
import com.example.spring.boot.service.ProductService;
import com.example.spring.boot.service.UserService;

import javax.annotation.Resource;
import java.util.List;


public class OrderServiceImpl implements OrderService {

    private UserService userService;

    private OrderRepository orderRepository;

    private ProductService productService;

    private ItemRepository itemRepository;

    private ShipmentRepository shipmentRepository;

    private RefundApplyRepository refundApplyRepository;

    private CommentRepository commentRepository;

    private ReturnApplyRepository returnApplyRepository;

    @Override
    public Order createOrder(User user, List<Item> items) throws CreateOrderException {

        Order order = Order.create(user, items);
        orderRepository.save(order);
        saveOrderItems(items, order);
        return order;
    }

    private void saveOrderItems(List<Item> items, Order order) {
        items.stream()
                .forEach(item -> {
                    item.setOrder(order);
                    itemRepository.save(item);
                });
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findOne(id);
    }

    @Override
    public Order pay(Long orderId) {
        Order order = findById(orderId);
        order.pay();
        return order;
    }


    @Override
    public Order claim(long orderId) {
        Order order = findById(orderId);
        order.claim();
        return order;
    }

    @Override
    public RefundApply applyRefund(Long orderId, RefundReason refundReason, String refundRemark) {
        Order order = findById(orderId);
        RefundApply apply = order.applyRefund(refundReason, refundRemark);
        refundApplyRepository.save(apply);
        return apply;
    }

    public Comment comment(Long orderId, CommentGrade grade, String remark, int score) {
        Order order = findById(orderId);
        Comment comment = order.comment(grade, remark, score);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Shipment ship(long orderId, String shipmentCompany, String shipmentSerial) {
        Order order = findById(orderId);
        Shipment shipment = order.ship(shipmentCompany, shipmentSerial);
        shipmentRepository.save(shipment);
        return shipment;
    }

    @Override
    public ReturnApply applyReturn(Long orderId, ReturnReason reason, String remark) {
        Order order = findById(orderId);
        ReturnApply apply = order.applyReturn(reason, remark);
        returnApplyRepository.save(apply);
        return apply;
    }

    @Resource
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Resource
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Resource
    public void setItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Resource
    public void setShipmentRepository(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Resource
    public void setRefundApplyRepository(RefundApplyRepository refundApplyRepository) {
        this.refundApplyRepository = refundApplyRepository;
    }

    @Resource
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Resource
    public void setReturnApplyRepository(ReturnApplyRepository returnApplyRepository) {
        this.returnApplyRepository = returnApplyRepository;
    }
}
