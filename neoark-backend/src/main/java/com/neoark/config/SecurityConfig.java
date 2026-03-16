package com.neoark.config;

import com.neoark.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    // 자동 로그인을 위해 UserService(UserDetailsService 구현체)를 주입받습니다.
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/join", "/api/users/login", "/login/**", "/oauth2/**").permitAll() 
                .anyRequest().authenticated() 
            )
            // 1. 자동 로그인 설정 (Remember-Me)
            .rememberMe(remember -> remember
                .key("neoark_secret_key_1234") // 쿠키 암호화 키
                .tokenValiditySeconds(60 * 60 * 24 * 30) // 30일간 유지
                .rememberMeParameter("remember-me") // 프론트 파라미터명
                .userDetailsService(userService) // 인증에 사용할 서비스
            )
            // 2. 소셜 로그인 설정 (OAuth2)
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/api/users/me") // 로그인 성공 시 이동할 곳
                .failureUrl("/login?error")        // 실패 시 이동할 곳
            )
            // 3. 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/api/users/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("remember-me", "JSESSIONID") // 쿠키 삭제
                .invalidateHttpSession(true)
            );

        return http.build();
    }
}