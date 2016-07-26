package com.example.spring.boot.domain;

import com.example.spring.boot.service.InsufficientProductStockException;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 现货订单
 */
@Entity
@DynamicInsert
@DynamicUpdate
@DiscriminatorValue("ACTUAL")
public class ActualOrder extends Order {

    @Enumerated(EnumType.STRING)
    private ActualOrderState state;

    @Column(precision = 20, scale = 2)
    private Double totalPrice;

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ActualOrderState getState() {
        return state;
    }

    public void setState(ActualOrderState state) {
        this.state = state;
    }

    public static ActualOrder create(User user, List<Item> items) throws InsufficientProductStockException {
        minusProductsStock(items);
        ActualOrder actualOrder = new ActualOrder();
        actualOrder.setUser(user);
        actualOrder.setItems(items);
        actualOrder.setTotalPrice(calculateTotalPrice(items));
        return actualOrder;
    }

    private static void minusProductsStock(List<Item> items) throws InsufficientProductStockException {
        for (Item item : items) {
            Product product = item.getProduct();
            if (!product.hasEnoughStock(item.getCount())) {
                throw new InsufficientProductStockException(String.format("product stock is not enough, expected %s, actual %s", item.getCount(), product.getStock()));
            }
            product.minusStock(item.getCount());
        }
    }

    private static Double calculateTotalPrice(List<Item> items) {
        return items.stream()
                .mapToDouble(item -> {
                    return item.getProduct().getPrice() * item.getCount();
                })
                .sum();
    }

    public void pay() {
        getState().pay(this);
    }


    @Override
    public void claim() {
        getState().claim(this);
    }

    @Override
    public RefundApply applyRefund(RefundReason refundReason, String refundRemark) {
        return getState().applyRefund(this, refundReason, refundRemark);
    }

    public boolean hasRefundApplyInProcess() {
        return Objects.nonNull(getRefundApplies()) && getRefundApplies().stream().anyMatch(apply -> RefundState.WAIT_AGREE.equals(apply.getState()));
    }

    @Override
    void refund() {
        getState().refund(this);
    }

    @Override
    public Comment comment(CommentGrade grade, String remark, int score) {
        return getState().comment(this, grade, remark, score);
    }

    @Override
    public Shipment ship(String shipmentCompany, String shipmentSerial) {
        return getState().ship(this, shipmentCompany, shipmentSerial);
    }

    @Override
    public ReturnApply applyReturn(ReturnReason reason, String remark) {
        return getState().applyReturn(this, reason, remark);
    }

    public boolean hasReturnApplyInProcess() {
        return Objects.nonNull(getReturnApplies()) && getReturnApplies().stream().anyMatch(apply -> ReturnState.WAIT_AGREE.equals(apply.getState()));
    }
}
