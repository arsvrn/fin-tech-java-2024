package com.tbank.edu.hw13.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final int tokenValiditySeconds = 30 * 24 * 60 * 60;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll() // Открытые эндпоинты для логина и регистрации
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Доступ к /api/admin/** только для ADMIN
                .anyRequest().authenticated() // Остальные требуют аутентификации
                .and()
                .formLogin()
                .loginProcessingUrl("/api/auth/login") // URL для логина
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/api/auth/logout") // URL для логаута
                .invalidateHttpSession(true) // Инвалидируем сессию
                .deleteCookies("JSESSIONID") // Удаляем cookie с идентификатором сессии
                .permitAll()
                .and()
                .rememberMe()
                .key("uniqueAndSecretKey")
                .tokenValiditySeconds(tokenValiditySeconds); // Сессия сохраняется на 30 дней при "запомнить меня"
        return http.build();
    }
}
