package com.example.spring.boot.domain;


public enum ActualOrderState {


    WAIT_PAYMENT {
        @Override
        public void pay(ActualOrder actualOrder) {
            actualOrder.setState(WAIT_SHIPMENT);
        }
    },

    WAIT_SHIPMENT {
        @Override
        public Shipment ship(ActualOrder actualOrder, String shipmentCompany, String shipmentSerial) {
            Shipment shipment = Shipment.create(actualOrder, shipmentCompany, shipmentSerial);
            actualOrder.setState(WAIT_CLAIM);
            return shipment;
        }

        @Override
        public RefundApply applyRefund(ActualOrder actualOrder, RefundReason refundReason, String refundRemark) {
            return doApplyRefund(actualOrder, refundReason, refundRemark);
        }

        @Override
        public void refund(ActualOrder actualOrder) {
            doRefund(actualOrder);
        }

    },

    WAIT_CLAIM {
        @Override
        public void claim(ActualOrder actualOrder) {
            actualOrder.setState(WAIT_COMMENT);
        }

        @Override
        public RefundApply applyRefund(ActualOrder actualOrder, RefundReason refundReason, String refundRemark) {
            return doApplyRefund(actualOrder, refundReason, refundRemark);
        }

        @Override
        public void refund(ActualOrder actualOrder) {
            doRefund(actualOrder);
        }

        @Override
        public ReturnApply applyReturn(ActualOrder actualOrder, ReturnReason reason, String remark) {
            return doApplyReturn(actualOrder, reason, remark);
        }
    },

    WAIT_COMMENT {
        @Override
        public void refund(ActualOrder actualOrder) {
            doRefund(actualOrder);
        }

        @Override
        public Comment comment(ActualOrder actualOrder, CommentGrade grade, String remark, int score) {
            Comment comment = Comment.create(actualOrder, grade, remark, score);
            actualOrder.setState(SUCCESS);
            return comment;
        }

        @Override
        public ReturnApply applyReturn(ActualOrder actualOrder, ReturnReason reason, String remark) {
            return doApplyReturn(actualOrder, reason, remark);
        }
    },
    SUCCESS {
        @Override
        public void refund(ActualOrder actualOrder) {
            doRefund(actualOrder);
        }

        @Override
        public ReturnApply applyReturn(ActualOrder actualOrder, ReturnReason reason, String remark) {
            return doApplyReturn(actualOrder, reason, remark);
        }
    },
    CANCELED;

    private static ReturnApply doApplyReturn(ActualOrder actualOrder, ReturnReason reason, String remark) {
        if (actualOrder.hasReturnApplyInProcess()){
            throw new IllegalStateException("apply return failed, actualOrder %s has return apply in process.");
        }
        if (actualOrder.hasRefundApplyInProcess()) {
            throw new IllegalStateException("apply return failed, actualOrder %s has refund apply in process.");
        }
        return ReturnApply.create(actualOrder, reason, remark);
    }

    private static void doRefund(ActualOrder actualOrder) {
        actualOrder.setState(CANCELED);
    }

    public RefundApply doApplyRefund(ActualOrder actualOrder, RefundReason refundReason, String refundRemark) {
        if (actualOrder.hasRefundApplyInProcess()) {
            throw new IllegalStateException("apply refund failed, actualOrder %s has refund apply in process.");
        }
        if (actualOrder.hasReturnApplyInProcess()){
            throw new IllegalStateException("apply refund failed, actualOrder %s has return apply in process.");
        }
        return RefundApply.create(actualOrder, refundReason, refundRemark);
    }


    public void pay(ActualOrder actualOrder) {
        throw new IllegalStateException(String.format("invalid pay operation for actualOrder %s with state %s", actualOrder.getId(), this));
    }


    public void claim(ActualOrder actualOrder) {
        throw new IllegalStateException(String.format("invalid claim operation for actualOrder %s with state %s", actualOrder.getId(), this));
    }

    public void refund(ActualOrder actualOrder) {
        throw new IllegalStateException(String.format("invalid refund operation for actualOrder %s with state %s", actualOrder.getId(), this));
    }

    public Comment comment(ActualOrder actualOrder, CommentGrade grade, String remark, int score) {
        throw new IllegalStateException(String.format("invalid comment operation for actualOrder %s with state %s", actualOrder.getId(), this));
    }

    public Shipment ship(ActualOrder actualOrder, String shipmentCompany, String shipmentSerial) {
        throw new IllegalStateException(String.format("invalid ship operation for actualOrder %s with state %s", actualOrder.getId(), this));
    }

    public RefundApply applyRefund(ActualOrder actualOrder, RefundReason refundReason, String refundRemark) {
        throw new IllegalStateException(String.format("invalid apply refund operation for actualOrder %s with state %s", actualOrder.getId(), this));
    }

    public ReturnApply applyReturn(ActualOrder actualOrder, ReturnReason reason, String remark) {
        throw new IllegalStateException(String.format("invalid apply return operation for actualOrder %s with state %s", actualOrder.getId(), this));
    }
}
