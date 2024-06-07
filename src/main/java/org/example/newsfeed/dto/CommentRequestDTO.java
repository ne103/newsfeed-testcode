package org.example.newsfeed.dto;

import lombok.Getter;
import org.example.newsfeed.entity.Comment;

@Getter
public class CommentRequestDTO {

    private Long userId;
    private String content;

    public Comment toEntity() {
        return
            Comment.builder()
                .userId(userId)
                .content(content)
                .build();

    }
}
