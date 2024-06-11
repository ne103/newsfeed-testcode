package org.example.newsfeed.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.entity.UserStatusEnum;
import org.example.newsfeed.repository.UserRepository;
import org.springframework.stereotype.Component;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class UserUtil {
    private final UserRepository userRepository;

    public User userVerifyById(Long Id) {
        User user = userRepository.findById(Id).orElseThrow(
            () -> new NullPointerException("등록되지 않은 계정입니다")
        );
        if(user.getStatus().equals(UserStatusEnum.WITHDRAWN.getStatus())){
            throw new NullPointerException("삭제된 계정입니다");
        }
        return user;
    }

    public User userVerifyByUserId(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(
            () -> new NullPointerException("등록되지 않은 계정입니다")
        );
        if(user.getStatus().equals(UserStatusEnum.WITHDRAWN.getStatus())){
            throw new NullPointerException("삭제된 계정입니다");
        }
        return user;
    }
}