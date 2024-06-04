package org.example.newsfeed.service;

import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.PostRequestDTO;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;

    public Post createPost(PostRequestDTO dto) {

        var newPost = dto.toEntity();
        return postRepository.save(newPost);
    }

    public Post updatePost(Long postId, PostRequestDTO dto) {

        Post post = postRepository.findById(postId).orElseThrow(IllegalAccessError::new);

        post.setContent(dto.getContent());

        return postRepository.save(post);

    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(IllegalAccessError::new);
        postRepository.delete(post);
    }

    public Post getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(IllegalAccessError::new);
        return post;

    }
}