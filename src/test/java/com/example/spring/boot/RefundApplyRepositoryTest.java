package com.example.spring.boot;

import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.OrderState;
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
    @Sql(statements = "insert into orders(id, state) values(1, 'WAIT_CLAIM')")
    public void saveRefundApply() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setState(OrderState.WAIT_CLAIM);
        RefundApply apply = RefundApply.create(order, RefundReason.INAPPROPRIATE, "不合适");
        refundApplyRepository.save(apply);
        assertThat(apply.getId()).isNotNull();
    }
}
