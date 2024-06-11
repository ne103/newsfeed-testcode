package org.example.newsfeed.exception;

import org.example.newsfeed.entity.Post;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String message) {
        super(message);
    }

}
