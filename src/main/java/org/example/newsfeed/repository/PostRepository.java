package org.example.newsfeed.repository;

import org.example.newsfeed.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

