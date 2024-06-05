package org.example.newsfeed.repository;

import java.util.List;
import java.util.Optional;
import org.example.newsfeed.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeleted(Long postId, Boolean deleted);

    void deleteByDeleted(Boolean deleted);

    List<Post> findByDeleted(Boolean deleted);
}

