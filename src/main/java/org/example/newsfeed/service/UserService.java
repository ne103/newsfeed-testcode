package org.example.newsfeed.service;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.dto.PasswordRequestDTO;
import org.example.newsfeed.dto.SignupRequestDTO;
import org.example.newsfeed.dto.UserRequestDTO;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.entity.UserStatusEnum;
import org.example.newsfeed.exception.AlreadyWithdrawnUserException;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.example.newsfeed.exception.PasswordMismatchException;
import org.example.newsfeed.exception.UserIdNotFoundException;
import org.example.newsfeed.exception.UserNotFoundException;
import org.example.newsfeed.jwt.JwtUtil;
import org.example.newsfeed.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    // 비밀번호 암호화
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // 회원가입
    public void signup(SignupRequestDTO requestDto) {
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
                throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
            }
            throw new IllegalArgumentException("알 수 없는 오류가 발생했습니다!");
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
        // 상태 변경 로그 추가
        System.out.println("User status changed to WITHDRAWN for userId: " + userId);
    }

    // 회원 탈퇴 (소프트 삭제)
    @Transactional
    public void deleteAccount(String userId, PasswordRequestDTO passwordRequestDto) {
        log.info("삭제된 계정");
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserIdNotFoundException("존재하지 않는 이용자입니다."));

        if (!passwordEncoder.matches(passwordRequestDto.getBeforePassword(), user.getPassword())) {
            log.info("회원탈퇴 취소");
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        user.setStatus(UserStatusEnum.WITHDRAWN);
        userRepository.save(user);
    }



    // 내 프로필 조회
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당하는 아이디가 존재하지 않습니다.")
        );
    }

    // 내 프로필 수정(이름, 한 줄 소개)
    @Transactional
    public void updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당하는 아이디가 존재하지 않습니다.")
        );
        user.updateUser(dto);
    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(Long id, PasswordRequestDTO dto) {

        if (dto.getUpdatePassword().equals(dto.getBeforePassword())) {
            throw new InvalidPasswordException("기존 패스워드와 일치하면 안됩니다.");
        }
        User user = userRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당하는 아이디가 존재하지 않습니다.")
        );
        // 패스워드 비교 후 일치하면 변경
        user.updatePassword(dto, passwordEncoder);
    }

    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public boolean isWithdrawn(User user) {
        return "탈퇴".equals(user.getStatus());
    }

    public String login(String userId, String password) {
        Optional<User> userOpt = findByUserId(userId);
        if (!userOpt.isPresent()) {
            throw new InvalidPasswordException("아이디나 비밀번호가 일치하지 않습니다.");
        }
        User user = userOpt.get();

        if (isWithdrawn(user)) {
            throw new UserNotFoundException("탈퇴한 사용자입니다.");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("아이디나 비밀번호가 일치하지 않습니다.");
        }

        return user.getUserId(); // 인증 성공 시 사용자 ID 반환
    }

}
