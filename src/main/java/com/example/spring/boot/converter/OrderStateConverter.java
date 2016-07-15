package com.example.spring.boot.converter;

import com.example.spring.boot.domain.state.OrderState;
import com.example.spring.boot.domain.state.WaitBuyerPayOrderState;

import javax.persistence.AttributeConverter;

/**
 * Created by fangtao on 16/7/13.
 */
public class OrderStateConverter implements AttributeConverter<OrderState, String> {
    @Override
    public String convertToDatabaseColumn(OrderState attribute) {
        return attribute.getState();
    }

    @Override
    public OrderState convertToEntityAttribute(String column) {
        if (column.equals(WaitBuyerPayOrderState.STATE)) {
            return new WaitBuyerPayOrderState();
        }
        return null;
    }
}
