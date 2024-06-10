package org.example.newsfeed.service;


import java.util.List;
import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.CommentRequestDTO;
import org.example.newsfeed.entity.Comment;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.CommentNotFoundException;
import org.example.newsfeed.exception.InvalidUserException;
import org.example.newsfeed.exception.PostNotFoundException;
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
        Post post = postRepository.findByIdAndDeleted(postId, Boolean.FALSE)
            .orElseThrow(() -> new PostNotFoundException("해당 게시물이 존재하지 않습니다."));
        newComment.setPost(post);
        return commentRepository.save(newComment);


    }

    public List<Comment> getComments(Long postId) {

        if (postRepository.findByIdAndDeleted(postId, Boolean.FALSE).isPresent()) {
            return commentRepository.findAllByPostId(postId);
        }else{
            throw new PostNotFoundException("해당 게시물이 존재하지 않습니다.");
        }


    }

    public Comment updateComment(Long commentId, CommentRequestDTO dto, User user) {

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));

        if (postRepository.findByIdAndDeleted(comment.getPost().getId(), Boolean.FALSE).isEmpty()) {
            throw new PostNotFoundException("해당 게시물이 존재하지 않습니다."); // 게시물 삭제되어있으면 댓글 작성 x
        }


        if (comment.getUser().getId().equals(user.getId())) {
            comment.setContent(dto.getContent());
        } else {
            throw new InvalidUserException("작성자가 아닙니다.");
        }

        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, User user) {

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("해당 댓글이 존재하지 않습니다."));

        if (postRepository.findByIdAndDeleted(comment.getPost().getId(), Boolean.FALSE).isEmpty()) {
            throw new PostNotFoundException("해당 게시물이 존재하지 않습니다."); // 게시물 삭제되어있으면 댓글 삭제x
        }

        if (comment.getUser().getId().equals(user.getId())) {
            commentRepository.delete(comment);
        } else {
            throw new InvalidUserException("작성자가 아닙니다.");
        }

        //commentRepository.deleteById(commentId);
    }
}
