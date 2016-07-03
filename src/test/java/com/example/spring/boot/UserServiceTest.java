package com.example.spring.boot;

import com.example.spring.boot.domain.User;
import com.example.spring.boot.repository.UserRepository;
import com.example.spring.boot.service.UserHasBeenRegisteredException;
import com.example.spring.boot.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    UserServiceImpl userService;
    String username = "username";
    String password = "password";
    String registedUsername = "registeredUsername";


    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl();
        User user = User.create(username,password);
        UserRepository userRepository = creteUserRepository(user);
        userService.setUserRepository(userRepository);
    }

    private UserRepository creteUserRepository(User user) {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsername(username)).thenReturn(null);
        doAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                User userEntity = invocation.getArgumentAt(0, User.class);
                userEntity.setId(new Random().nextLong());
                return userEntity;
            }
        }).when(userRepository).save(argThat(new UsernameEqMatcher(user)));

        when(userRepository.findByUsername(registedUsername)).thenReturn(User.create(registedUsername,password));

        return userRepository;
    }

    @Test
    public void registerSuccess() throws Exception {
        User user = userService.register(username, password);
        assertNotNull(user.getId());
    }

    @Test(expected = UserHasBeenRegisteredException.class)
    public void userHasBeenRegistered() throws Exception {
        userService.register(registedUsername, password);
    }


    class UsernameEqMatcher extends ArgumentMatcher<User>{
        User user;

        public UsernameEqMatcher(User user) {
            this.user = user;
        }

        @Override
        public boolean matches(Object argument) {
            User arg = (User) argument;
            return arg!=null && arg.getUsername().equals(user.getUsername());
        }
    }
}
