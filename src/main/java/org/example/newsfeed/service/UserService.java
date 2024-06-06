package org.example.newsfeed.service;

import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.UserRequestDTO;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.NotFoundException;
import org.example.newsfeed.exception.UserErrorCode;
import org.example.newsfeed.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당하는 아이디가 존재하지 않습니다.")
        );
    }

    @Transactional
    public void updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당하는 아이디가 존재하지 않습니다.")
        );
        user.updateUser(dto);
    }
}
