package org.example.newsfeed.service;

import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.dto.PasswordRequestDto;
import org.example.newsfeed.dto.SignupRequestDto;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.entity.UserStatusEnum;
import org.example.newsfeed.exception.AlreadyWithdrawnUserException;
import org.example.newsfeed.exception.PasswordMismatchException;
import org.example.newsfeed.exception.UserIdNotFoundException;
import org.example.newsfeed.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 비밀번호 암호화
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // 회원가입
    public void signup(SignupRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String password = encodePassword(requestDto.getPassword());

        User user = new User(
            userId,
            password,
            requestDto.getName(),
            requestDto.getEmail(),
            requestDto.getComment(),
            "",
            "",
            UserStatusEnum.ACTIVE
        );

        registerUser(user);
    }

    // DB 값 중복 입력 시 오류 처리
    private void registerUser(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            String errorMessage = Objects.requireNonNull(ex.getRootCause()).getMessage();
            if (errorMessage.contains("Duplicate entry") && errorMessage.contains("user_id")) {
                throw new IllegalArgumentException("ID already exists!");
            }
            throw new IllegalArgumentException("An unknown error occurred");
        }
    }

    // 회원 탈퇴 처리
    @Transactional
    public void withdrawUser(String userId, String password) {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserIdNotFoundException("존재하지 않는 이용자입니다."));

        if (UserStatusEnum.WITHDRAWN.equals(user.getStatus())) {
            throw new AlreadyWithdrawnUserException("이미 탈퇴된 사용자입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        user.setStatus(UserStatusEnum.WITHDRAWN);
        userRepository.save(user);
    }

    // 회원 탈퇴 (소프트 삭제)
    @Transactional
    public void deleteAccount(String userId, PasswordRequestDto passwordRequestDto) {
        log.info("삭제된 계정");
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserIdNotFoundException("존재하지 않는 이용자입니다."));

        if (!passwordEncoder.matches(passwordRequestDto.getPassword(), user.getPassword())) {
            log.info("회원탈퇴 취소");
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        user.setStatus(UserStatusEnum.WITHDRAWN);
        userRepository.save(user);
    }


}
