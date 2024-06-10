package org.example.newsfeed.repository;


import java.util.List;
import org.example.newsfeed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findAllByPostId(Long postId);
}
