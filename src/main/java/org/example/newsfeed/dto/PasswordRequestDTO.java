package org.example.newsfeed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PasswordRequestDTO {

    private String beforePassword;

    private String updatePassword;

}
