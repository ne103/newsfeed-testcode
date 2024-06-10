package org.example.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseDTO<T> {
    // Getters and setters
    private String statusCode;
    private String message;
    private T data;

    public ResponseDTO(String statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

}