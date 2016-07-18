package com.example.spring.boot.domain;


public enum OrderState {


    WAIT_PAYMENT {
        @Override
        public void pay(Order order) {
            order.setState(WAIT_SHIPMENT);
        }
    },

    WAIT_SHIPMENT {
        @Override
        public Shipment ship(Order order, String shipmentCompany, String shipmentSerial) {
            Shipment shipment = Shipment.create(order, shipmentCompany, shipmentSerial);
            order.setState(WAIT_CLAIM);
            return shipment;
        }

        @Override
        public RefundApply applyRefund(Order order, RefundReason refundReason, String refundRemark) {
            return doApplyRefund(order, refundReason, refundRemark);
        }

        @Override
        public void refund(Order order) {
            doRefund(order);
        }

    },

    WAIT_CLAIM {
        @Override
        public void claim(Order order) {
            order.setState(WAIT_COMMENT);
        }

        @Override
        public RefundApply applyRefund(Order order, RefundReason refundReason, String refundRemark) {
            return doApplyRefund(order, refundReason, refundRemark);
        }

        @Override
        public void refund(Order order) {
            doRefund(order);
        }

        @Override
        public ReturnApply applyReturn(Order order, ReturnReason reason, String remark) {
            return doApplyReturn(order, reason, remark);
        }
    },

    WAIT_COMMENT {
        @Override
        public void refund(Order order) {
            doRefund(order);
        }

        @Override
        public Comment comment(Order order, CommentGrade grade, String remark, int score) {
            Comment comment = Comment.create(order, grade, remark, score);
            order.setState(SUCCESS);
            return comment;
        }

        @Override
        public ReturnApply applyReturn(Order order, ReturnReason reason, String remark) {
            return doApplyReturn(order, reason, remark);
        }
    },
    SUCCESS {
        @Override
        public void refund(Order order) {
            doRefund(order);
        }

        @Override
        public ReturnApply applyReturn(Order order, ReturnReason reason, String remark) {
            return doApplyReturn(order, reason, remark);
        }
    },
    CANCELED;

    private static ReturnApply doApplyReturn(Order order, ReturnReason reason, String remark) {
        if (order.hasReturnApplyInProcess()){
            throw new IllegalStateException("apply return failed, order %s has return apply in process.");
        }
        if (order.hasRefundApplyInProcess()) {
            throw new IllegalStateException("apply return failed, order %s has refund apply in process.");
        }
        return ReturnApply.create(order, reason, remark);
    }

    private static void doRefund(Order order) {
        order.setState(CANCELED);
    }

    public RefundApply doApplyRefund(Order order, RefundReason refundReason, String refundRemark) {
        if (order.hasRefundApplyInProcess()) {
            throw new IllegalStateException("apply refund failed, order %s has refund apply in process.");
        }
        if (order.hasReturnApplyInProcess()){
            throw new IllegalStateException("apply refund failed, order %s has return apply in process.");
        }
        return RefundApply.create(order, refundReason, refundRemark);
    }


    public void pay(Order order) {
        throw new IllegalStateException(String.format("invalid pay operation for order %s with state %s", order.getId(), this));
    }


    public void claim(Order order) {
        throw new IllegalStateException(String.format("invalid claim operation for order %s with state %s", order.getId(), this));
    }

    public void refund(Order order) {
        throw new IllegalStateException(String.format("invalid refund operation for order %s with state %s", order.getId(), this));
    }

    public Comment comment(Order order, CommentGrade grade, String remark, int score) {
        throw new IllegalStateException(String.format("invalid comment operation for order %s with state %s", order.getId(), this));
    }

    public Shipment ship(Order order, String shipmentCompany, String shipmentSerial) {
        throw new IllegalStateException(String.format("invalid ship operation for order %s with state %s", order.getId(), this));
    }

    public RefundApply applyRefund(Order order, RefundReason refundReason, String refundRemark) {
        throw new IllegalStateException(String.format("invalid apply refund operation for order %s with state %s", order.getId(), this));
    }

    public ReturnApply applyReturn(Order order, ReturnReason reason, String remark) {
        throw new IllegalStateException(String.format("invalid apply return operation for order %s with state %s", order.getId(), this));
    }
}
