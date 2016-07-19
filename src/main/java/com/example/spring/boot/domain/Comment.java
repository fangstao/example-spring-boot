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
    private ActualOrder actualOrder;

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

    public ActualOrder getActualOrder() {
        return actualOrder;
    }

    public void setActualOrder(ActualOrder actualOrder) {
        this.actualOrder = actualOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static Comment create(ActualOrder actualOrder, CommentGrade grade, String remark, int score) {
        Comment comment = new Comment();
        comment.setActualOrder(actualOrder);
        comment.setGrade(grade);
        comment.setRemark(remark);
        comment.setScore(score);
        return comment;

    }
}
