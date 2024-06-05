package org.example.newsfeed.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.dto.SignupRequestDto;
import org.example.newsfeed.dto.WithdrawRequestDto;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.entity.UserStatusEnum;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.example.newsfeed.exception.UserIdNotFoundException;
import org.example.newsfeed.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


    // 사용자 ID 유효성 검사
    private void validateUserId(String userId) {
        if (userId.length() < 10 || userId.length() > 20) {
            throw new IllegalArgumentException("사용자 ID는 최소 10글자 이상, 최대 20글자 이하여야 합니다.");
        }

        if (!userId.matches("a-zA-Z0-9")) {
            throw new IllegalArgumentException("사용자 ID는 대소문자 포함 영문 + 숫자만을 허용합니다.");
        }
    }

    // 비밀번호 유효성 검사
    private void validatePassword(String password) {
        if (password.length() < 10) {
            throw new IllegalArgumentException("비밀번호는 최소 10글자 이상이어야 합니다.");
        }

        if (!password.matches(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$"
        )) {
            throw new IllegalArgumentException("비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.");
        }
    }


    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // ID 일치 여부 (유효성 검사)
    public void withdraw(WithdrawRequestDto requestDto) {
        User user = userRepository.findByUserId(requestDto.getUser_id())
            .orElseThrow(() -> new UserIdNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // 비밀번호 일치 여부
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        //회원 탈퇴(db에서 완전히 삭제)
        user.setStatus(UserStatusEnum.WITHDRAWN.name());
        userRepository.delete(user);
    }

    public void signup(SignupRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 사용자 ID 유효성 검사
        validateUserId(userId);

        // 비밀번호 유효성 검사
        validatePassword(password);

        // 중복 ID 체크
        Optional<User> checkUser_id = userRepository.findByUserId(userId);
        if (checkUser_id.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 회원가입 (회원 상태 ACTIVE로 설정)
        User user = new User();
        user.setUserId(requestDto.getUserId());
        user.setPassword(password);
        user.setStatus(UserStatusEnum.ACTIVE.name());
        userRepository.save(user);
    }


}

