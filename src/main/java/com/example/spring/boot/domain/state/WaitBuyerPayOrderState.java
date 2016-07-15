package com.example.spring.boot.domain.state;


public class WaitBuyerPayOrderState extends OrderState{
    public final static String STATE = "WAIT_BUYER_PAYMENT";

    @Override
    public String getState() {
        return STATE;
    }
}
