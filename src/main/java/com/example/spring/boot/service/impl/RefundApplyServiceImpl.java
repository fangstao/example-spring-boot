package com.example.spring.boot.service.impl;

import com.example.spring.boot.domain.RefundApply;
import com.example.spring.boot.repository.RefundApplyRepository;
import com.example.spring.boot.service.RefundApplyService;

import javax.annotation.Resource;

/**
 * Created by fangtao on 16/7/16.
 */
public class RefundApplyServiceImpl implements RefundApplyService {

    private RefundApplyRepository refundApplyRepository;

    @Override
    public RefundApply agree(Long refundId) {
        RefundApply apply = findById(refundId);
        apply.agree();
        return apply;
    }



    @Override
    public RefundApply findById(Long refundId) {
        return refundApplyRepository.findOne(refundId);
    }

    @Override
    public RefundApply refuse(Long refuseRefundId, String refuseRemark) {
        RefundApply apply = findById(refuseRefundId);
        apply.refuse(refuseRemark);
        return apply;
    }

    @Override
    public RefundApply cancel(Long refundId) {
        RefundApply apply = findById(refundId);
        apply.cancel();
        return apply;
    }

    @Resource
    public void setRefundApplyRepository(RefundApplyRepository refundApplyRepository) {
        this.refundApplyRepository = refundApplyRepository;
    }
}
