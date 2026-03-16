package com.neoark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 암호화 엔진 등록
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 초기 개발 단계에서는 CSRF 해제
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/join", "/api/users/login").permitAll() // 가입, 로그인은 허용
                .anyRequest().authenticated() // 나머지는 로그인이 필요함
            );
        return http.build();
    }
}