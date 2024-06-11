package org.example.newsfeed.exception;

public class InvalidUserException extends RuntimeException {

    public InvalidUserException(String message) {
        super(message);
    }
}
