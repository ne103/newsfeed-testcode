package org.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    private Long userId; // long?
    private String content;

    @Builder
    public Post(Long userId, String content) {
        this.userId = userId;
        this.content = content;

    }

    public void setContent(String content) {
        this.content = content;
    }

}