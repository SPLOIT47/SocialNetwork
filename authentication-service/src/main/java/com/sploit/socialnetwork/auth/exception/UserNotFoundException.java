package com.sploit.socialnetwork.auth.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super(String.format("User with email %s not found", email));
    }

    public UserNotFoundException(String username, String message) {
        super(message + " " + username);
    }
}
