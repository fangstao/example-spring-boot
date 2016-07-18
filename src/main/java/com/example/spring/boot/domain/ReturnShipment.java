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
public class ReturnShipment extends EntityBase{

    @OneToOne
    private ReturnApply returnApply;

    private String company;



}
