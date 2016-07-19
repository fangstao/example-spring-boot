package com.example.spring.boot.controller;

import com.example.spring.boot.domain.Product;

/**
 * Created by pc on 2016/7/19.
 */
public class ProductSummary {
    private String name;
    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public static ProductSummary create(Product product) {
        ProductSummary summary = new ProductSummary();
        summary.setName(product.getName());
        summary.setPrice(product.getPrice());
        return summary;
    }
}
