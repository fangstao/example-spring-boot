package com.example.spring.boot;

import com.example.spring.boot.domain.ActualOrder;
import com.example.spring.boot.domain.Comment;
import com.example.spring.boot.domain.CommentGrade;
import com.example.spring.boot.domain.ActualOrderState;
import com.example.spring.boot.repository.CommentRepository;
import com.example.spring.boot.repository.OrderRepository;
import com.example.spring.boot.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by fangtao on 16/7/17.
 */
public class ActualOrderCommentTest {
    private OrderServiceImpl orderService;
    private CommentRepository commentRepository;
    private OrderRepository orderRepository;
    private Long orderId = 1L;
    private Long orderIdWithInvalidState = 2L;
    private Long generatedId = 1L;
    private String commentRemark = "还没用,看着不错";
    private int score = 5;
    private CommentGrade commentGrade = CommentGrade.POSITIVE;

    @Before
    public void setUp() throws Exception {
        orderService = new OrderServiceImpl();
        commentRepository = mockCommentRepository();
        orderRepository = mockOrderRepository();
        orderService.setCommentRepository(commentRepository);
        orderService.setOrderRepository(orderRepository);
    }

    private OrderRepository mockOrderRepository() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findOne(orderId)).thenAnswer(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setState(ActualOrderState.WAIT_COMMENT);
                actualOrder.setId(orderId);
                return actualOrder;
            }
        });
        when(orderRepository.findOne(orderIdWithInvalidState)).thenAnswer(new Answer<ActualOrder>() {
            @Override
            public ActualOrder answer(InvocationOnMock invocation) throws Throwable {
                ActualOrder actualOrder = new ActualOrder();
                actualOrder.setId(orderIdWithInvalidState);
                actualOrder.setState(ActualOrderState.WAIT_SHIPMENT);
                actualOrder.setTotalPrice(18.0);
                return actualOrder;
            }
        });
        return orderRepository;
    }

    private CommentRepository mockCommentRepository() {
        CommentRepository commentRepository = mock(CommentRepository.class);
        when(commentRepository.save(any(Comment.class))).then(new Answer<Comment>() {
            @Override
            public Comment answer(InvocationOnMock invocation) throws Throwable {
                Comment comment = invocation.getArgumentAt(0, Comment.class);
                comment.setId(generatedId);
                return comment;
            }
        });
        return commentRepository;
    }

    @Test
    public void commentOrder() throws Exception {
        Comment comment = orderService.comment(orderId, commentGrade, commentRemark, score);
        assertThat(comment.getId()).isNotNull();
        assertThat(comment.getRemark()).isEqualTo(commentRemark);
        assertThat(comment.getScore()).isEqualTo(score);
        assertThat(comment.getGrade()).isEqualTo(commentGrade);
        assertThat(comment.getActualOrder().getId()).isEqualTo(orderId);
        assertThat(comment.getActualOrder().getState()).isEqualTo(ActualOrderState.SUCCESS);
    }

    @Test(expected = IllegalStateException.class)
    public void commentOrderWithInvalidState() throws Exception {
        Comment comment = orderService.comment(orderIdWithInvalidState, commentGrade, commentRemark, score);

    }
}
