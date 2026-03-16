package com.neoark.dto;

public class UserJoinRequest {
    private String email;
    private String password;
    private String nickname;

    // 기본 생성자 (Jackson 라이브러리가 JSON을 객체로 바꿀 때 필요함)
    public UserJoinRequest() {}

    // 모든 필드를 포함한 생성자
    public UserJoinRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    // Getter & Setter (Lombok이 없으므로 직접 작성)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}