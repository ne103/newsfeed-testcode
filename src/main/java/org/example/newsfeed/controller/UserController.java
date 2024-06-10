package org.example.newsfeed.controller;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.dto.ErrorResponseDTO;
import org.example.newsfeed.dto.PasswordRequestDTO;
import org.example.newsfeed.dto.SignupRequestDto;
import org.example.newsfeed.dto.UserRequestDTO;
import org.example.newsfeed.dto.UserResponseDTO;
import org.example.newsfeed.dto.WithdrawRequestDto;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.AlreadyWithdrawnUserException;
import org.example.newsfeed.exception.DuplicateUserException;
import org.example.newsfeed.exception.PasswordMismatchException;
import org.example.newsfeed.exception.UserErrorCode;
import org.example.newsfeed.exception.UserIdNotFoundException;
import org.example.newsfeed.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        try {
            userService.signup(requestDto);
        } catch (DuplicateUserException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "400");
            errorResponse.put("errorMessage", "중복된 사용자가 존재합니다.");
            return ResponseEntity.unprocessableEntity().body(errorResponse);
        }

        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("statusCode", "200");
        successResponse.put("message", "회원가입에 성공하였습니다.");
        return ResponseEntity.ok(successResponse);
    }

    @PatchMapping("/deletemembers")
    public ResponseEntity<?> withdrawUser(@RequestBody WithdrawRequestDto withdrawRequestDto) {
        try {
            userService.withdrawUser(withdrawRequestDto.getUserId(), withdrawRequestDto.getPassword());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원 탈퇴에 성공하였습니다. 감사합니다.");
            return ResponseEntity.ok(response);
        } catch (UserIdNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "404");
            errorResponse.put("errorMessage", "해당 사용자를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (PasswordMismatchException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "400");
            errorResponse.put("errorMessage", "아이디와 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (AlreadyWithdrawnUserException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", "400");
            errorResponse.put("errorMessage", "이미 탈퇴된 사용자입니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    // 내 프로필 조회
    @GetMapping("/profle/{id}")
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

    // 내 프로필 수정 (이름, 한 줄 소개)
    @PutMapping("profile/{id}")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.ok().body("프로필 수정에 성공하셨습니다.");
    }

    // 내 비밀번호 수정
    @PutMapping("profile/{id}/password")
    public ResponseEntity updatePassword(@PathVariable Long id, @RequestBody PasswordRequestDTO dto) {
        userService.updatePassword(id, dto);
        return ResponseEntity.ok().body("비밀번호 수정에 성공하였습니다.");
    }
}

