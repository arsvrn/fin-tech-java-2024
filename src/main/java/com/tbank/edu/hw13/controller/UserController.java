package com.tbank.edu.hw13.controller;

import com.tbank.edu.hw13.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        try {
            userService.registerUser(username, password);
            return "Пользователь успешно зарегистрирован";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/request-password-reset")
    public String requestPasswordReset(@RequestParam String username) {
        boolean userExists = userService.requestPasswordReset(username);
        if (userExists) {
            return "Код для сброса пароля отправлен (используйте '0000' для тестирования).";
        } else {
            return "Пользователь не найден";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String newPassword,
                                @RequestParam String code) {
        boolean resetSuccessful = userService.resetPassword(username, newPassword, code);
        if (resetSuccessful) {
            return "Пароль успешно изменен.";
        } else {
            return "Неправильный пароль или код.";
        }
    }
}
