package com.example.spring.boot.controller;

import com.example.spring.boot.domain.ActualOrder;
import com.example.spring.boot.domain.Item;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pc on 2016/7/19.
 */
public class OrderSummary {

    private List<ItemSummary> items;
    private Double totalPrice;

    public List<ItemSummary> getItems() {
        return items;
    }

    public void setItems(List<ItemSummary> items) {
        this.items = items;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static OrderSummary create(ActualOrder order) {
        OrderSummary summary = new OrderSummary();
        summary.setTotalPrice(order.getTotalPrice());
        List<ItemSummary> items = createItemSummaries(order.getItems());
        summary.setItems(items);
        return summary;
    }

    private static List<ItemSummary> createItemSummaries(List<Item> items) {
        return items.stream().map(ItemSummary::create).collect(Collectors.toList());
    }
}
