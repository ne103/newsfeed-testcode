package org.example.newsfeed.controller;

import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.SignupRequestDto;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String signup(SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return "redirect:/api/login";
    }




}
