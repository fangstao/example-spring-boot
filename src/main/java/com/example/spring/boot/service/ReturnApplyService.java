package com.example.spring.boot.service;

import com.example.spring.boot.domain.ReturnApply;
import org.hibernate.loader.custom.Return;

/**
 * Created by fangtao on 16/7/17.
 */
public interface ReturnApplyService {
    ReturnApply agree(Long applyId);

    ReturnApply findById(Long applyId);

    ReturnApply refuse(Long refuseReturnApplyId, String refuseRemark);
}
