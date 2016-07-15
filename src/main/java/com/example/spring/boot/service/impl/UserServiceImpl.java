package com.example.spring.boot.service.impl;

import com.example.spring.boot.domain.User;
import com.example.spring.boot.repository.UserRepository;
import com.example.spring.boot.service.RegisterException;
import com.example.spring.boot.service.UserHasBeenRegisteredException;
import com.example.spring.boot.service.UserService;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by fangtao on 16/7/3.
 */
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User register(String username, String password) throws RegisterException {
        Assert.hasText(username, "username must not blank");
        Assert.hasText(password, "password must not blank");
        User user = findByUsername(username);
        if (Objects.nonNull(user)) {
            throw new UserHasBeenRegisteredException(String.format("user with name %s has been registered", username));
        }
        user = User.create(username, password);
        userRepository.save(user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Resource
    public void setUserRepository(UserRepository userDao) {
        this.userRepository = userDao;
    }
}
