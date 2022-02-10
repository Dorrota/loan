package com.kocurek.loans.controller;

import com.kocurek.loans.domain.User;
import com.kocurek.loans.service.LoanService;
import com.kocurek.loans.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class LoanControllerTest {

    @Mock
    private LoanService loanService;
    @Mock
    private UserService userService;
    @InjectMocks
    private LoanController loanController;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        //mockMvc = MockMvcBuilders.webAppContextSetup(context)
        //        .apply(springSecurity()).build();
        mockMvc = MockMvcBuilders.standaloneSetup(this.loanController).build();
    }

    @Test
    @WithMockUser(username = "login", password = "123")
    public void getLoanDetailsTest() throws Exception {

        User user = new User(1L, "tralala", "name", "lastName", "123", new HashSet<>());
        lenient().when(userService.getUserByLogin(anyString())).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.get("/loan/form"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("loanrequest"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user));

    }
    @Test
    void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }
}
