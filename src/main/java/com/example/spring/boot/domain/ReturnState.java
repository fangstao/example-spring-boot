package com.example.spring.boot.domain;

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
    AGREED,
    REFUSED,
    CANCELED;

    public void agree(ReturnApply apply) {
        throw new IllegalStateException(String.format("illegal agree operation for state %s", this));
    }

    public void refuse(ReturnApply apply, String refuseRemark) {
        throw new IllegalStateException(String.format("illegal refuse operation for state %s", this));
    }
}
