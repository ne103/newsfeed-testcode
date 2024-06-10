package org.example.newsfeed.dto;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.entity.Comment;
@Getter
@Setter
public class CommentResponseDTO {
    private Long Id;
    private String userId;
    private Long postId;
    private String content;
    private Timestamp postDate;
    private Timestamp modifiedDate;
    public CommentResponseDTO(Comment comment) {
        this.Id = comment.getId();
        this.userId = comment.getUser().getUserId();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.postDate = comment.getCreatedAt();
        this.modifiedDate = comment.getUpdatedAt();

    }
}
