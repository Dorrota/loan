package com.kocurek.loans.controller;

import com.kocurek.loans.domain.User;
import com.kocurek.loans.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }
    @Test
    public void whenHomePageThanOkTest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/home"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenRegisterPageThanOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }

    @Test
    @WithMockUser
    public void lll() throws Exception {
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        User user = new User(1L, "Login", "name", "lastName", "Password", new HashSet<>());
        when(passwordEncoder.encode(anyString())).thenReturn("Password");
        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/register"))
        //.param("login", "Login")
        //.param("password", "Password"))
                //.andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));

    }
}
