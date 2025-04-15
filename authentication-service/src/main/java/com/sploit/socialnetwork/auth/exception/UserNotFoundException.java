package com.sploit.socialnetwork.auth.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(String.format("User with name or email %s not found", username));
    }

    public UserNotFoundException(String username, String message) {
        super(message + " " + username);
    }
}
