package com.example.spring.boot;

import com.example.spring.boot.domain.Item;
import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.OrderState;
import com.example.spring.boot.repository.OrderRepository;
import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.IASTORE;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.stream.Stream;

@DataJpaTest
@EnableJpaAuditing
@RunWith(SpringRunner.class)
public class OrderRepositoryTest {
    @Resource
    private OrderRepository orderRepository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void saveOrder() throws Exception {
        Order order = new Order();
        order.setState(OrderState.WAIT_PAYMENT);
        order.setTotalPrice(28.0);
        orderRepository.save(order);
        assertThat(order.getId()).isNotNull();
    }


    @Test
    @Sql(statements = {
            "insert into users (id, password, username) values (5, 'password', 'tommy')",
            "insert into products (id, name, price, stock) values(1, 'pizza', 18.5, 20)",
            "insert into products (id, name, price, stock) values(2, 'spaghetti', 5.82, 10)",
            "insert into orders (id, state, total_price, user_id) values (10, 'WAIT_PAYMENT', 18.2, 5)",
            "insert into items (id, product_id, order_id, count) values (1, 1, 10, 5)",
            "insert into items (id, product_id, order_id, count) values (2, 2, 10, 4)"
    })
    public void findOrder() throws Exception {
        Order order = orderRepository.findOne(10L);

        assertThat(order.getId()).isEqualTo(10L);
        assertThat(order.getState()).isEqualTo(OrderState.WAIT_PAYMENT);
        assertThat(order.getTotalPrice()).isEqualTo(18.2);
        assertThat(order.getUser().getId()).isEqualTo(5L);
        assertThat(order.getItems()).extracting(item -> item.getId()).hasSameElementsAs(Lists.newArrayList(1L, 2L));
        assertThat(order.getItems()).extracting(item -> item.getProduct().getId()).hasSameElementsAs(Lists.newArrayList(1L, 2L));
    }


}
