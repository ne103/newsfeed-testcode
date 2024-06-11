package org.example.newsfeed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.newsfeed.entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserResponseDTO {

    private String userId;

    private String password;

    private String name;

    private String comment;

    private String email;

    public UserResponseDTO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.comment = user.getComment();
        this.email = user.getEmail();
    }
}
