package com.example.spring.boot.service;

import com.example.spring.boot.domain.RefundApply;

/**
 *
 * Created by fangtao on 16/7/16.
 */
public interface RefundApplyService {
    RefundApply agree(Long refundId);

    RefundApply findById(Long refundId);

    RefundApply refuse(Long refuseRefundId, String refuseRemark);

    RefundApply cancel(Long cancelRefundId);
}
