package org.example.newsfeed.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.example.newsfeed.exception.UserNotFoundException;
import org.example.newsfeed.jwt.JwtUtil;
import org.example.newsfeed.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreate_date(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        user.setStatus("정상");
        return userRepository.save(user);
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
