package com.kocurek.loans.services;

import com.kocurek.loans.domain.User;
import com.kocurek.loans.repository.UserRepository;
import com.kocurek.loans.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnUserWhenSaveUser() {
        //given
        User user = new User(1L, "Login", "Name", "LastName", "Password", new HashSet<>());
        when(userRepository.save(user)).thenReturn(user);

        //when
        User savedUser = userService.saveUser(user);

        //then
        verify(userRepository, times(1)).save(user);
        assertEquals(user.getLastName(), savedUser.getLastName());
    }

    @Test
    public void getUserByLoginShouldFindUser() {
        //given
        String login = "Login";
        User user = new User(1L, login, "Name", "LastName", "Password", new HashSet<>());
        when(userRepository.findByLogin(login)).thenReturn(user);
        //when
        User userByLogin = userService.getUserByLogin(login);
        //then
        assertSame(userByLogin, user);
    }


}
