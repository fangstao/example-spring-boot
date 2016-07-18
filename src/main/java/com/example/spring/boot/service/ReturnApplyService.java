package com.example.spring.boot.service;

import com.example.spring.boot.domain.ReturnApply;
import com.example.spring.boot.domain.ReturnShipment;
import org.hibernate.loader.custom.Return;

/**
 * Created by fangtao on 16/7/17.
 */
public interface ReturnApplyService {
    ReturnApply agree(Long applyId);

    ReturnApply findById(Long applyId);

    ReturnApply refuse(Long applyId, String refuseRemark);

    ReturnShipment ship(Long applyId, String company, String serial);

    ReturnApply claim(Long applyId);
}
