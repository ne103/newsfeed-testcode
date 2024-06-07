package org.example.newsfeed.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) //영속성전이
    private List<Comment> comments = new ArrayList<Comment>();

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