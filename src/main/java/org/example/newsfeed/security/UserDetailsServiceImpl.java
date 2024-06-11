package org.example.newsfeed.security;

import org.example.newsfeed.entity.User;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.entity.UserStatusEnum;
import org.example.newsfeed.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // 사용자 객체 검색
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UsernameNotFoundException("Not Found " + userId));
        //
        if (user.getStatus().equals(UserStatusEnum.WITHDRAWN.getStatus())) {
            throw new UsernameNotFoundException("User Withdrawn" + userId);
        }

        return new UserDetailsImpl(user);

    }
    }


