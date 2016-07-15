package com.example.spring.boot.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DynamicUpdate
@DynamicInsert
public class Item extends EntityBase{
    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    private Integer count;

    @ManyToOne
    private User user;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
