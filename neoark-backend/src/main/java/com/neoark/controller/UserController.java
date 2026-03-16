package com.neoark.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neoark.entity.User;
import com.neoark.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 생성자 주입
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 접수
    @PostMapping("/join")
    public String join(@RequestBody User user) {
        userService.join(user);
        return "회원가입 완료: " + user.getNickname();
    }

    // 로그인 (임시 구조 - 실제로는 보안 설정이 추가되어야 함)
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        // 나중에 세션이나 JWT 발급 로직이 들어갑니다.
        return "로그인 시도 중...";
    }
}