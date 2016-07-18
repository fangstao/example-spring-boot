package com.example.spring.boot.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 *
 * Created by fangtao on 16/7/17.
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "comments")
public class Comment extends EntityBase{
    @OneToOne
    private Order order;

    private String remark;

    private Integer score;

    @Enumerated(EnumType.STRING)
    private CommentGrade grade;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public CommentGrade getGrade() {
        return grade;
    }

    public void setGrade(CommentGrade grade) {
        this.grade = grade;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static Comment create(Order order, CommentGrade grade, String remark, int score) {
        Comment comment = new Comment();
        comment.setOrder(order);
        comment.setGrade(grade);
        comment.setRemark(remark);
        comment.setScore(score);
        return comment;

    }
}
