package org.example.newsfeed.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.cookie.CookieUtil;
import org.example.newsfeed.dto.ErrorResponseDto;
import org.example.newsfeed.dto.LoginRequestDto;
import org.example.newsfeed.dto.ResponseDto;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.example.newsfeed.exception.InvalidTokenException;
import org.example.newsfeed.exception.UserNotFoundException;
import org.example.newsfeed.jwt.JwtUtil;
import org.example.newsfeed.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto.Login loginRequestDto, HttpServletResponse response) {
        try {
            String userId = userService.login(loginRequestDto.getUserId(), loginRequestDto.getPassword());
            String accessToken = jwtUtil.generateAccessToken(userId);
            String refreshToken = jwtUtil.generateRefreshToken(userId);

            // 쿠키에 토큰 설정: Access Token 30분, Refresh Token 2주
            CookieUtil.addCookie(response, "Authorization", accessToken, 30 * 60); // 30 minutes
            CookieUtil.addCookie(response, "Refresh-Token", refreshToken, 14 * 24 * 60 * 60); // 14 days
            log.info("로그인 시도");
            return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.toString(), "로그인에 성공하셨습니다.", null));
        } catch (UserNotFoundException | InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto("404", "로그인에 실패하였습니다.", e.getMessage())
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = CookieUtil.getCookieValue(request, "Authorization");
        String refreshToken = CookieUtil.getCookieValue(request, "Refresh-Token");

        if (accessToken != null && refreshToken != null) {
            try {
                // Clear the cookies
                CookieUtil.clearCookie(request, response, "Authorization");
                CookieUtil.clearCookie(request, response, "Refresh-Token");

                return ResponseEntity.ok(
                    new ResponseDto<>(HttpStatus.OK.toString(), "로그아웃이 성공적으로 되었습니다.", null));
            } catch (InvalidTokenException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponseDto("400", "로그아웃에 실패하였습니다.", e.getMessage())
                );
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto("400", "로그아웃에 실패하였습니다.", "유효하지 않은 토큰입니다.")
            );
        }
    }

}