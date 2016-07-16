package com.example.spring.boot.domain;

import com.mysema.commons.lang.Assert;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "products")
public class Product extends EntityBase {
    private Integer stock;

    private String name;

    @Column(precision = 20, scale = 2)
    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void minusStock(Integer expectedCount) {
        if (!hasEnoughStock(expectedCount)) {
            throw new IllegalArgumentException("no enough stock, expected " + expectedCount + ", but actual " + getStock());
        }
        this.setStock(getStock() - expectedCount);
    }

    public boolean hasEnoughStock(Integer expectedCount) {
        Assert.isTrue(expectedCount > 0, "expect positive, but " + expectedCount);
        return getStock() >= expectedCount;
    }
}
