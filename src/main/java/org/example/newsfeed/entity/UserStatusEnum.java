package org.example.newsfeed.entity;

import lombok.Getter;

@Getter
public enum UserStatusEnum {
    ACTIVE("정상 회원입니다."),
    WITHDRAWN("탈퇴한 회원입니다.");

    private final String status;

    UserStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}