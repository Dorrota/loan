package com.kocurek.loans.controller;

import com.kocurek.loans.domain.User;
import com.kocurek.loans.service.LoanService;
import com.kocurek.loans.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@WebMvcTest(LoanController.class)
public class LonaControllerNewTest {

    @MockBean
    private UserService userService;
    @MockBean
    private LoanService loanService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "login1", password = "123")
    public void getLoanDetailsTest() throws Exception {

        User user = new User(1L, "loloo", "name", "lastName", "123", new HashSet<>());
        when(userService.getUserByLogin(anyString())).thenReturn(user);
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
