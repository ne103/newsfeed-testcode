package org.example.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;

    public AuthResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
