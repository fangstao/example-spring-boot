package com.example.spring.boot.controller;

import com.example.spring.boot.domain.Item;

import java.util.List;

/**
 * Created by pc on 2016/7/19.
 */
public class ItemSummary {
    private ProductSummary product;
    private Integer count;

    public ProductSummary getProduct() {
        return product;
    }

    public void setProduct(ProductSummary product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public static ItemSummary create(Item item) {
        ItemSummary summary = new ItemSummary();
        summary.setCount(item.getCount());
        ProductSummary product = ProductSummary.create(item.getProduct());
        summary.setProduct(product);
        return summary;
    }
}
