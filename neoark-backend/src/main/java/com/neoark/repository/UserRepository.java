package com.neoark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neoark.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 유저를 찾는 기능을 추가합니다 (로그인/중복체크용)
    Optional<User> findByEmail(String email);
}