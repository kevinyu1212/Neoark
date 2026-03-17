package com.neoark.service;

import com.neoark.entity.User;
import com.neoark.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 구글에서 보낸 유저 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // DB에 있으면 가져오고, 없으면 새로 가입 (보안 처리 포함)
        userRepository.findByEmail(email)
                .map(entity -> {
                    // 기존 유저라면 이름 정도만 업데이트 (선택 사항)
                    entity.setNickname(name);
                    return userRepository.save(entity);
                })
                .orElseGet(() -> {
                    // 신규 유저라면 비밀번호 없이(소셜 전용) 저장
                    User newUser = new User(email, "OAUTH_USER", name);
                    return userRepository.save(newUser);
                });

        return oAuth2User;
    }
}