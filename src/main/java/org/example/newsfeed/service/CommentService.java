package org.example.newsfeed.service;


import java.util.List;
import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.CommentRequestDTO;
import org.example.newsfeed.entity.Comment;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.repository.CommentRepository;
import org.example.newsfeed.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment creatComment(Long postId, CommentRequestDTO dto, User user) {
        var newComment = dto.toEntity(user);
        Post post = postRepository.findById(postId).orElseThrow(IllegalArgumentException::new);
        newComment.setPost(post);
        return commentRepository.save(newComment);


    }

    public List<Comment> getComments(Long postId) {
        return commentRepository.findAllByPostId(postId);

    }

    public Comment updateComment(Long commentId, CommentRequestDTO dto, Long userId) {

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(IllegalArgumentException::new);

        if (comment.getUser().getId().equals(userId)) {
            comment.setContent(dto.getContent());
        } else {
            throw new IllegalArgumentException();
        }

        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long userId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(IllegalArgumentException::new);
        if(comment.getUser().getId().equals(userId)) {
            commentRepository.delete(comment);
        }
        else{
            throw new IllegalArgumentException();
        }

        //commentRepository.deleteById(commentId);
    }
}
