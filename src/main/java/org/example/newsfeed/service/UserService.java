package org.example.newsfeed.service;

import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.PasswordRequestDTO;
import org.example.newsfeed.dto.UserRequestDTO;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.example.newsfeed.exception.NotFoundException;
import org.example.newsfeed.exception.UserErrorCode;
import org.example.newsfeed.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        user.updatePassword(dto);
    }
}
