package com.example.spring.boot;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by pc on 2016/6/28.
 */
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTempalte) {
        this.jdbcTemplate = jdbcTempalte;
    }
}
