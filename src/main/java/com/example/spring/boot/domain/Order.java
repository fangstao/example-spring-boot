package com.example.spring.boot.domain;

import com.example.spring.boot.converter.OrderStateConverter;
import com.example.spring.boot.domain.state.OrderState;
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
    private Set<Item> items;

    @Convert(converter = OrderStateConverter.class)
    private OrderState orderState;

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public static Order create(User user, Set<Item> items) {
        Order order = new Order();
        order.setUser(user);
        order.setItems(items);
        return order;
    }

}
