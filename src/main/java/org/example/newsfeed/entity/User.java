package org.example.newsfeed.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Java에서는 Camel case 적용, name설정함으로써 sql에서는 Snake case 적용
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column
    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @Column
    private String comment;

    @Column
    private String status;

    @Column
    @Setter
    private String refreshToken;

    @Column
    private String statusChangeTime;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "user")
    private List<Newsfeed> newsfeeds = new ArrayList<>();

    public void setStatus(UserStatusEnum status) {
        if (!status.getStatus().equals(this.status)) {
            this.status = status.getStatus();
            this.modifyDate = LocalDateTime.now();
        }
    }

    public User(String userId, String password, String name, String email, String comment,
        String refreshToken, String statusChangeTime, UserStatusEnum status) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.comment = comment;
        this.refreshToken = refreshToken;
        this.statusChangeTime = statusChangeTime;
        this.status = status.getStatus();
    }
}
