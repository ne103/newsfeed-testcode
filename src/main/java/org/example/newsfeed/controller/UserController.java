package org.example.newsfeed.controller;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.dto.SignupRequestDto;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.DuplicateUserException;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.example.newsfeed.exception.UserErrorCode;
import org.example.newsfeed.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;


    //회원가입 페이지 -> html파일 강의에서 쓰던거 쓸 것인가 추후 정하기
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", UserErrorCode.INVALID_PASSWORD_FORMAT);
            errorResponse.put("errorMessage", "회원가입에 실패하였습니다.");
            errorResponse.put("details", fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", ")));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            userService.signup(requestDto);
        } catch (DuplicateUserException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", UserErrorCode.DUPLICATE_USER);
            errorResponse.put("errorMessage", "회원가입에 실패하였습니다. 중복된 사용자가 존재합니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (InvalidPasswordException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorCode", UserErrorCode.INVALID_PASSWORD);
            errorResponse.put("errorMessage", "회원가입에 실패하였습니다. 비밀번호 형식이 올바르지 않습니다.");
            return ResponseEntity.unprocessableEntity().body(errorResponse);
        }

        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("statusCode", "200");
        successResponse.put("message", "회원가입에 성공하였습니다.");
        return ResponseEntity.ok(successResponse);
    }
}