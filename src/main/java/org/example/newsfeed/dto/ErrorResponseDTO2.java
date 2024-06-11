package org.example.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponseDTO2 {
    // Getters and setters
    private String errorCode;
    private String errorMessage;
    private String details;

    public ErrorResponseDTO2(String errorCode, String errorMessage, String details) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.details = details;
    }

}

