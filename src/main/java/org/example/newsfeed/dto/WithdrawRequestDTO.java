package org.example.newsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawRequestDTO {
    @NotBlank
    private String userId;
    @NotBlank
    private String password;
}
