package org.example.newsfeed.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.dto.SignupRequestDto;
import org.example.newsfeed.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public static void signup(SignupRequestDto requestDto) {

    }
}
