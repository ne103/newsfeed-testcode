package org.example.newsfeed.controller;

import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.UserRequestDTO;
import org.example.newsfeed.dto.UserResponseDTO;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.dto.ErrorResponseDTO;
import org.example.newsfeed.exception.UserErrorCode;
import org.example.newsfeed.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    public final UserService userService;

    // 내 프로필 조회
    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable Long id) {
        User user;
        try {
            user = userService.getUser(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ErrorResponseDTO.builder()
                .code(String.valueOf(UserErrorCode.USER_NOT_FOUND.getHttpStatus().value()))
                .message(UserErrorCode.USER_NOT_FOUND.getMessage())
                .build());
        }
        UserResponseDTO response = new UserResponseDTO(user);
        return ResponseEntity.ok(response);
    }

    // 내 프로필 수정 (이름, 이메일, 한 줄 소개)
    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.ok().body("프로필 수정에 성공하셨습니다.");
    }

        // 내 비밀번호 수정
    @PutMapping("/{id}/password")
    public ResponseEntity updatePassword(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        userService.updatePassword(id, dto);
        return ResponseEntity.ok().body("비밀번호 수정에 성공하였습니다.");

    }
}
