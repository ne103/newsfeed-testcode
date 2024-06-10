package org.example.newsfeed.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final UserErrorCode errorCode;

    public CustomException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
