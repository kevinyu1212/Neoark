package com.neoark.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    private int xp = 0;

    @Column(length = 20)
    private String tier = "BEGINNER";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // --- 생성자 (Constructor) ---
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String email, String password, String nickname) {
        this();
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    // --- Getter & Setter (Lombok 없이 직접 작성) ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}