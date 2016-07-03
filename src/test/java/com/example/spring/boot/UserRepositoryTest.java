package com.example.spring.boot;

import com.example.spring.boot.config.TestApplication;
import com.example.spring.boot.domain.QUser;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.repository.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.*;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

/**
 * 加载类 -> 解析 -> 初始化,装配
 */
@DataJpaTest
@ActiveProfiles("development")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestApplication.class})
public class UserRepositoryTest {
    @Resource
    UserRepository userRepository;

    @Test
    @Sql(statements = {
            "insert into users(username,password) values('tommy','tommy123')"
    })
    public void findUserByUsername() throws Exception {
        String username = "tommy";
        User user = userRepository.findByUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void saveUser() throws Exception {
        User user = User.create("jack", "rose");
        userRepository.save(user);
        assertNotNull(user.getId());
    }
}
