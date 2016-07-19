package com.example.spring.boot.domain;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "refund_applies")
public class RefundApply extends EntityBase {

    @ManyToOne
    private Order order;

    @Enumerated(EnumType.STRING)
    private RefundReason reason;

    private String remark;

    @Enumerated(EnumType.STRING)
    private RefundState state;

    private String refuseRemark;

    public String getRefuseRemark() {
        return refuseRemark;
    }

    public void setRefuseRemark(String refuseRemark) {
        this.refuseRemark = refuseRemark;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public RefundReason getReason() {
        return reason;
    }

    public void setReason(RefundReason reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RefundState getState() {
        return state;
    }

    public void setState(RefundState state) {
        this.state = state;
    }

    public static RefundApply create(ActualOrder actualOrder, RefundReason refundReason, String refundRemark) {
        RefundApply apply = new RefundApply();
        apply.setOrder(actualOrder);
        apply.setReason(refundReason);
        apply.setRemark(refundRemark);
        apply.setState(RefundState.WAIT_AGREE);
        return apply;
    }

    public void agree() {
        getState().agree(this);
    }

    public void refuse(String refuseRemark) {
        getState().refuse(this, refuseRemark);
    }

    public void cancel() {
        getState().cancel(this);
    }
}
