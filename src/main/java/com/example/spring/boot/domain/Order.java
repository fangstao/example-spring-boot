package com.example.spring.boot.domain;



import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.util.List;


/**
 * Created by pc on 2016/7/19.
 */
@Entity
@Table(name="orders")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "order_type")
public abstract class Order extends EntityBase{
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order")
    private List<Item> items;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<RefundApply> refundApplies;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<ReturnApply> returnApplies;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        getItems().add(item);
    }

    public List<RefundApply> getRefundApplies() {
        return refundApplies;
    }

    public void setRefundApplies(List<RefundApply> refundApplies) {
        this.refundApplies = refundApplies;
    }

    public List<ReturnApply> getReturnApplies() {
        return returnApplies;
    }

    public void setReturnApplies(List<ReturnApply> returnApplies) {
        this.returnApplies = returnApplies;
    }

    abstract void refund();

    public abstract Comment comment(CommentGrade grade, String remark, int score);

    public abstract Shipment ship(String shipmentCompany, String shipmentSerial);

    public abstract void claim();

    public abstract RefundApply applyRefund(RefundReason refundReason, String refundRemark);

    public abstract ReturnApply applyReturn(ReturnReason reason, String remark);
}
