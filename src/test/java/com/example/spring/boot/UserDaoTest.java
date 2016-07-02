package com.example.spring.boot;

import com.example.spring.boot.config.DaoConfig;
import com.example.spring.boot.config.ServiceConfig;
import com.example.spring.boot.config.TestDataSourceConfig;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.repository.UserDao;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Transactional
@ActiveProfiles("development")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDataSourceConfig.class, DaoConfig.class, ServiceConfig.class})
public class UserDaoTest {
    @Resource
    public UserDao userDao;
    @Mock
    public UserDao userDaoMock;

    @Test

    public void saveUser() {
        User user = new User();
        String username = "tommy";
        user.setName(username);
        userDao.save(user);
        assertNotNull(user.getId());
        assertEquals(username, user.getName());
    }

    @Test

    @Sql(statements = {"INSERT  INTO users(id, name) VALUES (1, 'tommy')"})
    public void findUserById() throws Exception {
        Integer userId = 1;
        User user = userDao.findById(userId);
        assertEquals(userId, user.getId());
    }
}
