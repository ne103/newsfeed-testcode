package org.example.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponseDto {
    // Getters and setters
    private String errorCode;
    private String errorMessage;
    private String details;

    public ErrorResponseDto(String errorCode, String errorMessage, String details) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.details = details;
    }

}
