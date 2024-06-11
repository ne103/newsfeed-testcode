package org.example.newsfeed.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.example.newsfeed.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeleted(Long postId, Boolean deleted);

    List<Post> findByDeleted(Boolean deleted);

    Page<Post> findAllByDeleted(Boolean deleted, Pageable pageable);

    Page<Post> findAllByDeletedAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(Boolean aFalse, Timestamp timestamp,
        Timestamp timestamp1, Pageable pageable);
}

