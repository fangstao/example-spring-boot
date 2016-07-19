package com.example.spring.boot;

import com.example.spring.boot.domain.ActualOrder;
import com.example.spring.boot.domain.ActualOrderState;
import com.example.spring.boot.domain.Comment;
import com.example.spring.boot.domain.CommentGrade;
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
    @Sql(statements = "insert into orders(id, state, order_type) values(1, 'WAIT_COMMENT', 'ACTUAL')")
    public void saveComment() throws Exception {
        ActualOrder actualOrder = new ActualOrder();
        actualOrder.setId(1L);
        actualOrder.setState(ActualOrderState.WAIT_COMMENT);
        Comment comment = Comment.create(actualOrder, CommentGrade.POSITIVE, "很好,还没用", 5);
        commentRepository.save(comment);
        assertThat(actualOrder.getId()).isNotNull();
    }
}
