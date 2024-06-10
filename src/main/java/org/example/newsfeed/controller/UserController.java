package org.example.newsfeed.controller;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.dto.SignupRequestDto;
import org.example.newsfeed.dto.WithdrawRequestDto;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.AlreadyWithdrawnUserException;
import org.example.newsfeed.exception.DuplicateUserException;
import org.example.newsfeed.exception.PasswordMismatchException;
import org.example.newsfeed.exception.UserIdNotFoundException;
import org.example.newsfeed.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

}



