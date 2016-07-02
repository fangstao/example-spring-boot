package com.example.spring.boot;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by pc on 2016/6/28.
 */
@Repository
public class UserDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public void save(final User user) {

        jdbcTemplate.update("INSERT  INTO users(name) VALUES (?)", user.getName());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO users(name) VALUES(?)");
                statement.setString(1, user.getName());
                return statement;
            }
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
    }

    public UserDao() {
    }

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findById(Integer userId) {
        List<User> users = jdbcTemplate.query("select id, name from users where id = ?", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                return User.create(id,name);
            }
        }, userId);
        return users.size() > 0 ? users.get(0) : null;
    }

}
