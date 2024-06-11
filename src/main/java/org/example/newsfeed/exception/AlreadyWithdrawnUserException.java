package org.example.newsfeed.exception;

public class AlreadyWithdrawnUserException extends RuntimeException{
    public AlreadyWithdrawnUserException(String message) {
        super(message);
    }

}
