package org.example.newsfeed.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonErrorResponseDTO {
    // Getters and setters
    private String errorCode;
    private String errorMessage;
    private String details;

    public CommonErrorResponseDTO(String errorCode, String errorMessage, String details) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.details = details;
    }
}
