package org.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long userId; // ?


    private String content;

    private boolean deleted = Boolean.FALSE;

    @Builder
    public Post(Long userId, String content) {
        this.userId = userId;
        this.content = content;

    }

    public void setDeleted(){
        this.deleted = Boolean.TRUE;
    }
    public void setContent(String content) {
        this.content = content;
    }

}