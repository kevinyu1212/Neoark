package com.neoark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.neoark.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    // [수정] CustomOAuth2UserService 주입 제거
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/favicon.ico", "/error");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .httpBasic(basic -> basic.disable()) // 팝업창 방지
            .authorizeHttpRequests(auth -> auth
                // 소셜 로그인 관련 경로(/oauth2/**) 제거 및 기본 경로 허용
                .requestMatchers("/", "/login", "/api/users/join", "/api/users/login").permitAll() 
                .anyRequest().permitAll() // [임시] 일단 모든 접근 허용하여 로딩/인증 문제 해결
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/api/users/me", true)
                .permitAll()
            )
            // [삭제] .oauth2Login(...) 섹션 전체 제거
            .rememberMe(remember -> remember
                .key("neoark_secret_key_1234") 
                .tokenValiditySeconds(60 * 60 * 24 * 30) 
                .rememberMeParameter("remember-me") 
                .userDetailsService(userService) 
            )
            .logout(logout -> logout
                .logoutUrl("/api/users/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("remember-me", "JSESSIONID")
                .invalidateHttpSession(true)
            );

        return http.build();
    }
}