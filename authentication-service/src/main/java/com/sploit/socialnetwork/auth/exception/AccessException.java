package com.sploit.socialnetwork.auth.exception;

public class AccessException extends RuntimeException {
    public AccessException(String userId) {
        super("User " + userId + " are blocked");
    }
}
