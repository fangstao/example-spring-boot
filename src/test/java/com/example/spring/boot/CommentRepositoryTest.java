package com.example.spring.boot;

import com.example.spring.boot.domain.Comment;
import com.example.spring.boot.domain.CommentGrade;
import com.example.spring.boot.domain.Order;
import com.example.spring.boot.domain.OrderState;
import com.example.spring.boot.repository.CommentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.*;


/**
 * Created by fangtao on 16/7/17.
 */
@DataJpaTest
@EnableJpaAuditing
@RunWith(SpringRunner.class)
public class CommentRepositoryTest {
    @Resource
    private CommentRepository commentRepository;

    @Test
    @Sql(statements = "insert into orders(id, state) values(1, 'WAIT_COMMENT')")
    public void saveComment() throws Exception {
        Order order = new Order();
        order.setId(1L);
        ;
        order.setState(OrderState.WAIT_COMMENT);
        Comment comment = Comment.create(order, CommentGrade.POSITIVE, "很好,还没用", 5);
        commentRepository.save(comment);
        assertThat(order.getId()).isNotNull();
    }
}
