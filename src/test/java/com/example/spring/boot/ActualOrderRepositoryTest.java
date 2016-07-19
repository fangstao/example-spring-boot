package com.example.spring.boot;

import com.example.spring.boot.domain.ActualOrder;
import com.example.spring.boot.domain.ActualOrderState;
import com.example.spring.boot.repository.OrderRepository;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

import javax.annotation.Resource;

@DataJpaTest
@EnableJpaAuditing
@RunWith(SpringRunner.class)
public class ActualOrderRepositoryTest {
    @Resource
    private OrderRepository orderRepository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void saveOrder() throws Exception {
        ActualOrder actualOrder = new ActualOrder();
        actualOrder.setState(ActualOrderState.WAIT_PAYMENT);
        actualOrder.setTotalPrice(28.0);
        orderRepository.save(actualOrder);
        assertThat(actualOrder.getId()).isNotNull();
    }


    @Test
    @Sql(statements = {
            "insert into users (id, password, username) values (5, 'password', 'tommy')",
            "insert into products (id, name, price, stock) values(1, 'pizza', 18.5, 20)",
            "insert into products (id, name, price, stock) values(2, 'spaghetti', 5.82, 10)",
            "insert into orders (id, state, total_price, user_id, order_type) values (10, 'WAIT_PAYMENT', 18.2, 5, 'ACTUAL')",
            "insert into items (id, product_id, order_id, count) values (1, 1, 10, 5)",
            "insert into items (id, product_id, order_id, count) values (2, 2, 10, 4)"
    })
    public void findOrder() throws Exception {
        ActualOrder actualOrder = orderRepository.findOne(10L);

        assertThat(actualOrder.getId()).isEqualTo(10L);
        assertThat(actualOrder.getState()).isEqualTo(ActualOrderState.WAIT_PAYMENT);
        assertThat(actualOrder.getTotalPrice()).isEqualTo(18.2);
        assertThat(actualOrder.getUser().getId()).isEqualTo(5L);
        assertThat(actualOrder.getItems()).extracting(item -> item.getId()).hasSameElementsAs(Lists.newArrayList(1L, 2L));
        assertThat(actualOrder.getItems()).extracting(item -> item.getProduct().getId()).hasSameElementsAs(Lists.newArrayList(1L, 2L));
    }


}
