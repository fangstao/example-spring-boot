package com.example.spring.boot.service.impl;

import com.example.spring.boot.domain.ReturnApply;
import com.example.spring.boot.domain.ReturnShipment;
import com.example.spring.boot.repository.ReturnApplyRepository;
import com.example.spring.boot.repository.ReturnShipmentRepository;
import com.example.spring.boot.service.ReturnApplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by fangtao on 16/7/17.
 */
@Service
public class ReturnApplyServiceImpl implements ReturnApplyService {
    private ReturnApplyRepository returnApplyRepository;
    private ReturnShipmentRepository returnShipmentRepository;


    @Override
    public ReturnApply agree(Long returnApplyId) {
        ReturnApply apply = findById(returnApplyId);
        apply.agree();
        return apply;
    }

    @Override
    public ReturnApply findById(Long applyId) {
        return returnApplyRepository.findOne(applyId);
    }

    @Override
    public ReturnApply refuse(Long applyId, String refuseRemark) {
        ReturnApply apply = findById(applyId);
        apply.refuse(refuseRemark);
        return apply;
    }

    @Override
    public ReturnShipment ship(Long applyId, String company, String serial) {
        ReturnApply apply = findById(applyId);
        ReturnShipment shipment = apply.ship(company,serial);
        returnShipmentRepository.save(shipment);
        return shipment;
    }

    @Resource
    public void setReturnApplyRepository(ReturnApplyRepository returnApplyRepository) {
        this.returnApplyRepository = returnApplyRepository;
    }

    @Resource
    public void setReturnShipmentRepository(ReturnShipmentRepository returnShipmentRepository) {
        this.returnShipmentRepository = returnShipmentRepository;
    }
}
