package com.example.spring.boot;

import com.example.spring.boot.domain.ActualOrder;
import com.example.spring.boot.domain.ActualOrderState;
import com.example.spring.boot.domain.RefundApply;
import com.example.spring.boot.domain.RefundReason;
import com.example.spring.boot.repository.RefundApplyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class RefundApplyRepositoryTest {
    @Resource
    private RefundApplyRepository refundApplyRepository;

    @Test
    @Sql(statements = "insert into orders(id, state, order_type) values(1, 'WAIT_CLAIM', 'ACTUAL')")
    public void saveRefundApply() throws Exception {
        ActualOrder actualOrder = new ActualOrder();
        actualOrder.setId(1L);
        actualOrder.setState(ActualOrderState.WAIT_CLAIM);
        RefundApply apply = RefundApply.create(actualOrder, RefundReason.INAPPROPRIATE, "不合适");
        refundApplyRepository.save(apply);
        assertThat(apply.getId()).isNotNull();
    }
}
