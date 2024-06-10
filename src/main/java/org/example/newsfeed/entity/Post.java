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

//    @Column(nullable = false)
//    private Long userId; // ?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // ?

    private String content;

    private boolean deleted = Boolean.FALSE;

    @Builder
    public Post(User user, String content) {
        this.user = user;
        this.content = content;

    }

    public void setDeleted(){
        this.deleted = Boolean.TRUE;
    }
    public void setContent(String content) {
        this.content = content;
    }

}