package com.example.spring.boot.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by fangtao on 16/7/17.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "return_shipments")
public class ReturnShipment extends EntityBase {
    @OneToOne
    private ReturnApply apply;
    private String company;
    private String serial;

    public ReturnApply getApply() {
        return apply;
    }

    public void setApply(ReturnApply apply) {
        this.apply = apply;
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

    public static ReturnShipment create(ReturnApply apply, String company, String serial) {
        ReturnShipment shipment = new ReturnShipment();
        shipment.setApply(apply);
        shipment.setCompany(company);
        shipment.setSerial(serial);
        return shipment;
    }
}
