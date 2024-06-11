package org.example.newsfeed.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class LoginRequestDTO {
    private String userId;
    private String password;


    @Setter
    @Getter
    public static class Login {
        @NotEmpty(message = "Username cannot be empty")
        private String userId;

        @NotEmpty(message = "Password cannot be empty")
        private String password;

    }



    @Getter
    @Setter
    public static class Logout {
        @NotEmpty(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotEmpty(message = "잘못된 요청입니다.")
        private String refreshToken;
    }

}