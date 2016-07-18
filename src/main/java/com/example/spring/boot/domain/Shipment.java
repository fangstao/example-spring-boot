package com.example.spring.boot.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "shipments")
public class Shipment extends EntityBase {

    @OneToOne
    private Order order;
    private String company;
    private String serial;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public static Shipment create(Order order,String company, String serial) {
        Shipment ship = new Shipment();
        ship.setOrder(order);
        ship.setCompany(company);
        ship.setSerial(serial);
        return ship;
    }
}
