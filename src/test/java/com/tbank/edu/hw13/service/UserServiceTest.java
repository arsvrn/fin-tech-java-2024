package com.tbank.edu.hw13.service;

import com.tbank.edu.hw13.model.User;
import com.tbank.edu.hw13.repositoy.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        User user = new User(null, "testuser", "encodedPassword", "USER");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser("testuser", "password");

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testResetPasswordWithCorrectCode() {
        User user = new User(1L, "testuser", "oldPassword", "USER");
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        boolean result = userService.resetPassword("testuser", "newPassword", "0000");

        assertTrue(result);
        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testResetPasswordWithIncorrectCode() {
        boolean result = userService.resetPassword("testuser", "newPassword", "1234");
        assertFalse(result);
    }
}