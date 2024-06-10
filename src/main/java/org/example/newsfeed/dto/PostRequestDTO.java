package org.example.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.entity.User;

@Getter
@Setter
public class PostRequestDTO {

    //private Long userId;
    private String content;

    public Post toEntity(User user) {
        return Post.builder()
            .user(user)
            .content(content)
            .build();

    }
}

