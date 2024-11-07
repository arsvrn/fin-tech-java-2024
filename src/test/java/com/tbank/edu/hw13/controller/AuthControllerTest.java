package com.tbank.edu.hw13.controller;

import com.tbank.edu.hw13.model.User;
import com.tbank.edu.hw13.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() throws Exception {
        when(userService.registerUser("testuser", "password")).thenReturn(new User(null, "testuser", "encodedPassword", "USER"));

        mockMvc.perform(post("/api/auth/register")
                        .param("username", "testuser")
                        .param("password", "password")
                        .param("role", "USER"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testRequestPasswordReset() throws Exception {
        when(userService.requestPasswordReset("testuser")).thenReturn(true);

        mockMvc.perform(post("/api/auth/request-password-reset")
                        .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset code sent (use '0000' for testing)."));
    }

    @Test
    public void testResetPasswordWithCorrectCode() throws Exception {
        when(userService.resetPassword("testuser", "newPassword", "0000")).thenReturn(true);

        mockMvc.perform(post("/api/auth/reset-password")
                        .param("username", "testuser")
                        .param("newPassword", "newPassword")
                        .param("code", "0000"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully."));
    }

    @Test
    public void testResetPasswordWithIncorrectCode() throws Exception {
        when(userService.resetPassword("testuser", "newPassword", "1234")).thenReturn(false);

        mockMvc.perform(post("/api/auth/reset-password")
                        .param("username", "testuser")
                        .param("newPassword", "newPassword")
                        .param("code", "1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid username or code."));
    }
}