package org.example.newsfeed.service;

import java.util.regex.Pattern;
import org.example.newsfeed.dto.SignupRequestDto;
import org.example.newsfeed.dto.WithdrawRequestDto;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.entity.UserRoleEnum;
import org.example.newsfeed.entity.UserStatusEnum;
import org.example.newsfeed.exception.AlreadyWithdrawnUserException;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.example.newsfeed.exception.PasswordMismatchException;
import org.example.newsfeed.exception.UserIdNotFoundException;
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


    // 사용자 ID 유효성 검사
    private void validateUserId(String userId) {
        if (userId.length() < 10 || userId.length() > 20) {
            throw new IllegalArgumentException("사용자 ID는 최소 10글자 이상, 최대 20글자 이하여야 합니다.");
        }
        if (!userId.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("사용자 ID는 대소문자 포함 영문, 숫자만을 허용합니다.");
        }
    }

    // 비밀번호 유효성 검사
    private void validatePassword(String password) {
        if (password.length() < 10) {
            throw new IllegalArgumentException("비밀번호는 최소 10글자 이상이어야 합니다.");
        }

        if (Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$", password)) {
            throw new IllegalArgumentException("비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.");
        }

    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // ID 일치 여부 (유효성 검사)
    public void withdraw(WithdrawRequestDto requestDto) {
        User user = userRepository.findByUserId(requestDto.getUserId())
            .orElseThrow(() -> new UserIdNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // 비밀번호 일치 여부
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        //회원 탈퇴 및 상태 변경
        user.setStatus(UserStatusEnum.WITHDRAWN.name());
        userRepository.save(user);
    }

    public void signup(SignupRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 사용자 ID 유효성 검사
        validateUserId(userId);

        // 비밀번호 유효성 검사
        validatePassword(password);

        // 중복 ID 체크
        Optional<User> checkUser_id = userRepository.findByUserIdAndStatus(requestDto.getUserId(),
            UserStatusEnum.ACTIVE.name());
        if (checkUser_id.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 회원가입 (회원 상태 ACTIVE로 설정)
        User user = new User(requestDto.getUserId(), requestDto.getPassword(),
            UserStatusEnum.ACTIVE.name(),
            UserRoleEnum.USER);
        userRepository.save(user);
    }

    //Active상태만 조회
    Optional<User> findByUserIdAndStatus(String userId, String status) {
        return userRepository.findByUserIdAndStatus(userId, status);
    }


    private boolean isExistingOrWithdrawnId(String userId) {
        Optional<User> existingUser = userRepository.findByUserId(userId);
        if (existingUser.isPresent()) {
            return true; // 중복된 ID
        }

         // *************************** 탈퇴한 ID => Status가 Active인 걸 찾으면 되잖어~

        return false; // 사용가능한 ID
    }


    public void withdrawUser(String userId, String password) {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserIdNotFoundException("존재하지 않는 이용자입니다."));

        if (user.isWithdraw()) {
            throw new AlreadyWithdrawnUserException("이미 탈퇴된 사용자입니다.");
        }

        if (!user.getPassword().equals(password)) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        user.withdraw();
        userRepository.save(user);
    }

}

