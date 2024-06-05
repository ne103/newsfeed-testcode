package org.example.newsfeed.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.dto.SignupRequestDto;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.example.newsfeed.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(SignupRequestDto requestDto) {
        String user_id = requestDto.getUser_id();
        String password = passwordEncoder.encode(requestDto.getPassword());

        //사용자 ID 유효성 검사
        validateUserId(requestDto.getUser_id());

        // 중복 ID 체크
        Optional<User> checkUser_id = userRepository.findByUserId(user_id);
        if (checkUser_id.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        //회원가입
        User user = new User();
        user.setUserId(requestDto.getUser_id());
        user.setPassword(requestDto.getPassword());
        userRepository.save(user);
    }

    private void validateUserId(String userId) {
        if(userId.length() < 10 || userId.length() > 20) {
            throw new IllegalArgumentException("사용자 ID는 최소 10글자 이상, 최대 20글자 이하여야 합니다.");
        }

        if(userId.matches("a-zA-Z0-9")) {
            throw new IllegalArgumentException("사용자 ID는 대소문자 포함 영문 + 숫자만을 허용합니다.");
        }
    }


}





//        // email 중복확인
//        String email = requestDto.getEmail();
//        Optional<User> checkEmail = userRepository.findByEmail(email);
//        if (checkEmail.isPresent()) {
//            throw new IllegalArgumentException("중복된 Email 입니다.");
//        }
