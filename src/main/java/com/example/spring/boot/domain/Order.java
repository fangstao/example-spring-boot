package com.example.spring.boot.domain;

import com.example.spring.boot.service.InsufficientProductStockException;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "orders")
public class Order extends EntityBase {

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order")
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Column(precision = 20, scale = 2)
    private Double totalPrice;

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }


    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

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
        items.add(item);
    }


    public static Order create(User user, List<Item> items) throws InsufficientProductStockException {
        minusProductsStock(items);
        Order order = new Order();
        order.setUser(user);
        order.setItems(items);
        order.setTotalPrice(calculateTotalPrice(items));
        return order;
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
}
