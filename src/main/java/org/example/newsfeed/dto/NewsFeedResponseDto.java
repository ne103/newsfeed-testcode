package org.example.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewsFeedResponseDto {
    private String message;
    private int statusCode;

    public NewsFeedResponseDto(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
