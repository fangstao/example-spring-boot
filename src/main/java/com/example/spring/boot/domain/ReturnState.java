package com.example.spring.boot.domain;

import org.hibernate.loader.custom.Return;

import java.util.Objects;

/**
 * Created by fangtao on 16/7/17.
 */
public enum ReturnState {
    WAIT_AGREE {
        @Override
        public void agree(ReturnApply apply) {
            apply.setState(ReturnState.AGREED);
        }

        @Override
        public void refuse(ReturnApply apply, String refuseRemark) {
            apply.setRefuseRemark(refuseRemark);
            apply.setState(ReturnState.REFUSED);
        }
    },
    AGREED {
        @Override
        public ReturnShipment ship(ReturnApply apply, String company, String serial) {
            ReturnShipment shipment = ReturnShipment.create(apply, company, serial);
            apply.setState(WAIT_CLAIM);
            return shipment;
        }
    },
    WAIT_CLAIM{
        @Override
        public void claim(ReturnApply apply) {
            Order order = apply.getOrder();
            order.refund();
            apply.setState(ReturnState.CLAIMED);
        }
    },
    REFUSED,
    CANCELED, CLAIMED;

    public void agree(ReturnApply apply) {
        throw new IllegalStateException(String.format("illegal agree operation for state %s", this));
    }

    public void refuse(ReturnApply apply, String refuseRemark) {
        throw new IllegalStateException(String.format("illegal refuse operation for state %s", this));
    }

    public ReturnShipment ship(ReturnApply apply, String company, String serial) {
        throw new IllegalStateException(String.format("illegal ship operation for state %s", this));
    }

    public void claim(ReturnApply apply) {
        throw new IllegalStateException(String.format("illegal claim operation for state %s", this));
    }
}
