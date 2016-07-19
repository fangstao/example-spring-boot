package com.example.spring.boot.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "shipments")
public class Shipment extends EntityBase {

    @OneToOne
    private ActualOrder order;
    private String company;
    private String serial;

    public ActualOrder getOrder() {
        return order;
    }

    public void setOrder(ActualOrder order) {
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

    public static Shipment create(ActualOrder actualOrder, String company, String serial) {
        Shipment ship = new Shipment();
        ship.setOrder(actualOrder);
        ship.setCompany(company);
        ship.setSerial(serial);
        return ship;
    }
}
