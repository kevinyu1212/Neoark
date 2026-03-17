package com.neoark.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        // 직접 만든 login.html 파일이 없다면, 
        // 일단 설정을 확인하기 위해 시큐리티 기본 페이지로 리다이렉트 시키거나
        // 뷰 이름을 리턴해야 합니다.
        return "login"; 
    }
}