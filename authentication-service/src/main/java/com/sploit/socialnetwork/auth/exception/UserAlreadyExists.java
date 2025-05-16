package com.sploit.socialnetwork.auth.exception;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String email) {
        super("user with email: " + email + " already exists");
    }
}
