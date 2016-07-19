package com.example.spring.boot.service.impl;

import com.example.spring.boot.domain.*;
import com.example.spring.boot.repository.*;
import com.example.spring.boot.service.CreateOrderException;
import com.example.spring.boot.service.OrderService;
import com.example.spring.boot.service.ProductService;
import com.example.spring.boot.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
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
    @Transactional(readOnly = false, rollbackFor = Throwable.class)
    public ActualOrder createOrder(User user, List<Item> items) throws CreateOrderException {
        replaceProductsWithEntity(items);
        ActualOrder actualOrder = ActualOrder.create(user, items);
        orderRepository.save(actualOrder);
        saveOrderItems(items, actualOrder);
        return actualOrder;
    }

    private void replaceProductsWithEntity(List<Item> items) {
        items.forEach(item -> {
            Product product = productService.findById(item.getProduct().getId());
            item.setProduct(product);
        });
    }

    private void saveOrderItems(List<Item> items, ActualOrder actualOrder) {
        for (Item item : items) {
            item.setOrder(actualOrder);
            itemRepository.save(item);
        }
    }

    @Override
    public ActualOrder findById(Long id) {
        return orderRepository.findOne(id);
    }

    @Override
    public ActualOrder pay(Long orderId) {
        ActualOrder actualOrder = findById(orderId);
        actualOrder.pay();
        return actualOrder;
    }


    @Override
    public ActualOrder claim(long orderId) {
        ActualOrder actualOrder = findById(orderId);
        actualOrder.claim();
        return actualOrder;
    }

    @Override
    public RefundApply applyRefund(Long orderId, RefundReason refundReason, String refundRemark) {
        ActualOrder actualOrder = findById(orderId);
        RefundApply apply = actualOrder.applyRefund(refundReason, refundRemark);
        refundApplyRepository.save(apply);
        return apply;
    }

    public Comment comment(Long orderId, CommentGrade grade, String remark, int score) {
        ActualOrder actualOrder = findById(orderId);
        Comment comment = actualOrder.comment(grade, remark, score);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Shipment ship(long orderId, String shipmentCompany, String shipmentSerial) {
        ActualOrder actualOrder = findById(orderId);
        Shipment shipment = actualOrder.ship(shipmentCompany, shipmentSerial);
        shipmentRepository.save(shipment);
        return shipment;
    }

    @Override
    public ReturnApply applyReturn(Long orderId, ReturnReason reason, String remark) {
        ActualOrder actualOrder = findById(orderId);
        ReturnApply apply = actualOrder.applyReturn(reason, remark);
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
