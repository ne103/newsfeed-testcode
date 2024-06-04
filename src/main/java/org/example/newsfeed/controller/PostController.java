package org.example.newsfeed.controller;

import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.PostRequestDTO;
import org.example.newsfeed.dto.PostResponseDTO;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {

    public final PostService postService;

    @PostMapping
    public ResponseEntity postPost(@RequestBody PostRequestDTO dto) {
        Post post = postService.createPost(dto);
        PostResponseDTO response = new PostResponseDTO(post);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{postId}")
    public ResponseEntity getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        PostResponseDTO response = new PostResponseDTO(post);
        return ResponseEntity.ok(response);
    }


    @PutMapping("{postId}")
    public ResponseEntity updatePost(@PathVariable Long postId, @RequestBody PostRequestDTO dto) {
        Post post = postService.updatePost(postId, dto);
        PostResponseDTO response = new PostResponseDTO(post);
        return ResponseEntity.ok(response);
    }



    @DeleteMapping("{postId}")
    public void updatePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

}
