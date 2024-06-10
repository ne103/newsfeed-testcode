package org.example.newsfeed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.entity.UserStatusEnum;

@Getter
@Setter
public class SignupRequestDto {


    @Pattern(regexp = "^[A-Za-z\\d]{10,20}$")
    private String userId;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*?.,;:])[A-Za-z\\d!@#$%^&*?.,;:]{10,}$")
    private String password;

    private String name;
    @Email
    private String email;
    private String comment;


}