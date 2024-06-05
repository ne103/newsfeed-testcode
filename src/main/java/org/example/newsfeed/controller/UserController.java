package org.example.newsfeed.controller;

import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.UserResponseDTO;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class UserController {

    public final UserService userService;

    // 내 프로필 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        UserResponseDTO response = new UserResponseDTO(user);
        return ResponseEntity.ok(response);
    }

    // 내 프로필 수정 (이름, 이메일, 한 줄 소개

}
