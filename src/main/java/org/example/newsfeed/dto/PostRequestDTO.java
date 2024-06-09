package org.example.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.entity.Post;

@Getter
@Setter
public class PostRequestDTO {

    //private Long userId;
    private String content;

    public Post toEntity(Long userId) {
        return Post.builder()
            .userId(userId)
            .content(content)
            .build();

    }
}

