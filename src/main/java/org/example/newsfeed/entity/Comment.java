package org.example.newsfeed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "comment_id", nullable = false)
    private Long id;


    private Long userId;
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; //N:1


    @Builder
    public Comment(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }


    public void setPost(Post post) {
        this.post = post;
    }

    public void setContent(String content){
        this.content = content;
    }
}
