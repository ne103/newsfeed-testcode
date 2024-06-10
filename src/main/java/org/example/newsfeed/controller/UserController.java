package org.example.newsfeed.controller;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.cookie.CookieUtil;
import org.example.newsfeed.dto.ErrorResponseDTO;
import org.example.newsfeed.dto.LoginRequestDTO;
import org.example.newsfeed.dto.PasswordRequestDTO;
import org.example.newsfeed.dto.ResponseDTO;
import org.example.newsfeed.dto.SignupRequestDTO;
import org.example.newsfeed.dto.UserRequestDTO;
import org.example.newsfeed.dto.UserResponseDTO;
import org.example.newsfeed.dto.WithdrawRequestDTO;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.AlreadyWithdrawnUserException;
import org.example.newsfeed.exception.DuplicateUserException;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.example.newsfeed.exception.InvalidTokenException;
import org.example.newsfeed.exception.CommonErrorResponseDTO;
import org.example.newsfeed.exception.PasswordMismatchException;
import org.example.newsfeed.exception.UserErrorCode;
import org.example.newsfeed.exception.UserIdNotFoundException;
import org.example.newsfeed.exception.UserNotFoundException;
import org.example.newsfeed.jwt.JwtUtil;
import org.example.newsfeed.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDTO requestDto) {
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
    public ResponseEntity<?> withdrawUser(@RequestBody WithdrawRequestDTO withdrawRequestDto) {
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
    @GetMapping("/profile/{id}")
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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO.Login loginRequestDto, HttpServletResponse response) {
        try {
            String userId = userService.login(loginRequestDto.getUserId(), loginRequestDto.getPassword());
            String accessToken = jwtUtil.generateAccessToken(userId);
            String refreshToken = jwtUtil.generateRefreshToken(userId);

            // 쿠키에 토큰 설정: Access Token 30분, Refresh Token 2주
            CookieUtil.addCookie(response, "Authorization", accessToken, 30 * 60); // 30 minutes
            CookieUtil.addCookie(response, "Refresh-Token", refreshToken, 14 * 24 * 60 * 60); // 14 days
            log.info("로그인 시도");
            return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.OK.toString(), "로그인에 성공하셨습니다.", null));
        } catch (UserNotFoundException | InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new CommonErrorResponseDTO("404", "로그인에 실패하였습니다.", e.getMessage())
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
                    new ResponseDTO<>(HttpStatus.OK.toString(), "로그아웃이 성공적으로 되었습니다.", null));
            } catch (InvalidTokenException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new CommonErrorResponseDTO("400", "로그아웃에 실패하였습니다.", e.getMessage())
                );
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new CommonErrorResponseDTO("400", "로그아웃에 실패하였습니다.", "유효하지 않은 토큰입니다.")
            );
        }
    }

}


