package org.example.newsfeed.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 없습니다."),
    PW_MISMATCH(HttpStatus.NOT_FOUND, "유저이름과 비밀번호 불일치"),
    PASSWORD_MISMATCH(HttpStatus.NOT_FOUND, "현재 비밀번호와 불일치"),
    PASSWORD_MATCH(HttpStatus.NOT_FOUND, "현재 비밀번호와 일치"),
    WITHDRAW_USER(HttpStatus.NOT_FOUND, "이미 탈퇴한 회원입니다."),
    DUPLICATED_USER(HttpStatus.CONFLICT, "중복된 회원입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글의 정보를 찾을수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
