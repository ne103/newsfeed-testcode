package org.example.newsfeed.dto;

import lombok.Getter;
import org.example.newsfeed.entity.Comment;
import org.example.newsfeed.entity.User;

@Getter
public class CommentRequestDTO {


    private String content;

    public Comment toEntity(User user) {
        return
            Comment.builder()
                .user(user)
                .content(content)
                .build();

    }
}
