package com.example.spring.boot.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by fangtao on 16/7/17.
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "return_applies")
public class ReturnApply extends EntityBase {

    @ManyToOne
    private Order order;

    @Enumerated(EnumType.STRING)
    private ReturnReason reason;

    private String remark;

    @Enumerated(EnumType.STRING)
    private ReturnState state;

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

    public ReturnReason getReason() {
        return reason;
    }

    public void setReason(ReturnReason reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ReturnState getState() {
        return state;
    }

    public void setState(ReturnState state) {
        this.state = state;
    }

    public static ReturnApply create(Order order, ReturnReason reason, String remark) {
        ReturnApply apply = new ReturnApply();
        apply.setOrder(order);
        apply.setReason(reason);
        apply.setRemark(remark);
        apply.setState(ReturnState.WAIT_AGREE);
        return apply;
    }

    public void agree() {
        getState().agree(this);
    }

    public void refuse(String refuseRemark) {
        getState().refuse(this, refuseRemark);
    }
}
