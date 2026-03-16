package com.neoark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neoark.entity.User;
import com.neoark.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    // 생성자 주입 (Lombok이 없으므로 직접 작성)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입 로직
    @Transactional
    public User join(User user) {
        // 이메일 중복 체크 같은 로직을 여기에 넣습니다.
        return userRepository.save(user);
    }
    
    // 유저 조회 로직
    public User findOne(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
    }
}