package org.example.newsfeed.service;

import lombok.AllArgsConstructor;
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
            () -> new IllegalArgumentException("ㅠㅠ..ㅠㅠ..............ㅠㅠ..아이디가..없저..")
        );
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("ㅠㅠ..ㅠㅠ..............ㅠㅠ..아이디가..없저..")
        );
    }
}
