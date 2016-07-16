package com.example.spring.boot.domain;


public enum OrderState {


    WAIT_PAYMENT {
        @Override
        public void pay(Order order) {
            order.setState(WAIT_SHIPMENT);
        }
    },

    WAIT_SHIPMENT

    ;
    public void pay(Order order) {
        throw new IllegalStateException(String.format("invalid pay operation for order %s with state %s", order.getId(), this));
    }
}
