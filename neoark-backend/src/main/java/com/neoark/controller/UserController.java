package com.neoark.controller;

import com.neoark.dto.LoginRequest;
import com.neoark.dto.UserJoinRequest;
import com.neoark.entity.User;
import com.neoark.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. 회원가입 (DTO 사용)
    @PostMapping("/join")
    public String join(@RequestBody UserJoinRequest request) {
        User user = new User(request.getEmail(), request.getPassword(), request.getNickname());
        userService.join(user);
        return "회원가입 완료: " + user.getNickname();
    }

    // 2. 로그인
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        User user = userService.login(request.getEmail(), request.getPassword());
        
        // 세션 생성
        HttpSession session = httpRequest.getSession();
        session.setAttribute("LOGIN_USER", user);
        
        return user.getNickname() + "님, 로그인 성공!";
    }

    // 3. 로그아웃
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        return "로그아웃 되었습니다.";
    }

    // 4. 회원탈퇴
    @DeleteMapping("/withdraw")
    public String withdraw(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            return "로그인이 필요합니다.";
        }

        User loginUser = (User) session.getAttribute("LOGIN_USER");
        userService.withdraw(loginUser.getId());
        session.invalidate(); // 탈퇴 후 세션 삭제

        return "회원 탈퇴가 완료되었습니다.";
    }

    // 5. 내 정보 조회 (테스트용)
    @GetMapping("/me")
    public String me(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            return "로그인 상태가 아닙니다.";
        }
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        return "현재 로그인 유저: " + loginUser.getNickname() + " (" + loginUser.getTier() + ")";
    }
}