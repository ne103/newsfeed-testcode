package org.example.newsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawRequestDto {
    @NotBlank
    private String user_id;
    @NotBlank
    private String password;
}
