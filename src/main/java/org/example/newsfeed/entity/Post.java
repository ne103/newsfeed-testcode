package org.example.newsfeed.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference//
    private User user;  // ?

    private String content;

    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) //영속성전이
    private List<Comment> comments = new ArrayList<>();

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