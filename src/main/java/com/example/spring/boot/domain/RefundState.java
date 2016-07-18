package com.example.spring.boot.domain;

/**
 *
 * Created by fangtao on 16/7/16.
 */
public enum RefundState {
    WAIT_AGREE {
        @Override
        public void agree(RefundApply refundApply) {
            Order order = refundApply.getOrder();
            order.refund();
            refundApply.setState(AGREED);
        }

        @Override
        public void refuse(RefundApply refundApply, String refuseRemark) {
            refundApply.setRefuseRemark(refuseRemark);
            refundApply.setState(REFUSED);
        }

        @Override
        public void cancel(RefundApply refundApply) {
            refundApply.setState(CANCELED);
        }
    },
    AGREED,
    REFUSED,
    CANCELED;

    public void agree(RefundApply refundApply) {
        throw new IllegalStateException(String.format("invalid agree operation for refund apply %s with state %s", refundApply.getId(), this));
    }


    public void refuse(RefundApply refundApply, String refuseRemark) {
        throw new IllegalStateException(String.format("invalid refuse operation for refund apply %s with state %s", refundApply.getId(), this));
    }

    public void cancel(RefundApply refundApply) {
        throw new IllegalStateException(String.format("invalid cancel operation for refund apply %s with state %s", refundApply.getId(), this));
    }
}
