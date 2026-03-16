package com.neoark.service;

import com.neoark.entity.User;
import com.neoark.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService { // 1. 인터페이스 구현 추가

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Spring Security의 자동 로그인 및 인증에서 사용하는 핵심 메서드
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 이메일입니다: " + email));

        // Security 전용 User 객체로 변환하여 반환
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER") // 기본 권한 설정
                .build();
    }

    /**
     * 회원가입
     */
    @Transactional
    public User join(User user) {
        validateDuplicateUser(user);

        // 비밀번호 암호화 후 저장
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    /**
     * 로그인 검증
     */
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    /**
     * 중복 회원 체크
     */
    private void validateDuplicateUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 단건 조회
     */
    @Transactional(readOnly = true)
    public Optional<User> findOne(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void withdraw(Long userId) {
        userRepository.deleteById(userId);
    }
}