package com.neoark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.neoark.service.CustomOAuth2UserService;
import com.neoark.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(UserService userService, CustomOAuth2UserService customOAuth2UserService) {
        this.userService = userService;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // [중요] 파비콘이나 에러 페이지 등은 시큐리티 필터를 아예 거치지 않게 합니다.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/favicon.ico", "/error");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            // [해결포인트 1] 팝업창을 뜨게 하는 기본 인증 기능을 끕니다.
            .httpBasic(basic -> basic.disable()) 
            .authorizeHttpRequests(auth -> auth
                // [해결포인트 2] 로그인 관련 모든 경로를 완전히 개방합니다.
                .requestMatchers("/", "/login", "/login/**", "/oauth2/**", "/api/users/join", "/api/users/login").permitAll() 
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/api/users/me", true)
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService) 
                )
                .defaultSuccessUrl("/api/users/me", true) 
                .failureUrl("/login?error")
            )
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