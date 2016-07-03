package com.example.spring.boot.repository;

import java.util.Date;

/**
 * Created by fangtao on 16/7/2.
 */
public class MessageSpecification {
    private String phoneEq;
    private Date creationDateAfter;
    private String fromIp;
    public MessageSpecification phoneEq(String phone) {
        this.phoneEq = phone;
        return this;
    }

    public MessageSpecification creationDateAfter(Date fromDate) {
        this.creationDateAfter = fromDate;
        return this;
    }
    public static MessageSpecification create() {
        return new MessageSpecification();
    }

    public MessageSpecification fromIpEq(String fromIp) {
        this.fromIp = fromIp;
        return this;
    }

    public String getPhoneEq() {
        return phoneEq;
    }

    public Date getCreationDateAfter() {
        return creationDateAfter;
    }

    public String getFromIp() {
        return fromIp;
    }
}
