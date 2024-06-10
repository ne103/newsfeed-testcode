package org.example.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.entity.Post;

import java.sql.Timestamp;

@Getter
@Setter
public class PostResponseDTO {

    private Long Id;
    private String userId;
    private String content;
    private Timestamp postDate;
    private Timestamp modifiedDate;

    public PostResponseDTO(Post post) {

        this.Id = post.getId();
        this.userId = post.getUser().getUserId();
        this.content = post.getContent();
        this.postDate = post.getCreatedAt();
        this.modifiedDate = post.getUpdatedAt();

    }


}