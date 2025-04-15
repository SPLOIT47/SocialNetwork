package com.sploit.socialnetwork.auth.exception;

import javax.management.relation.Role;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String role) {
        super(String.format("Role %s not found", role));
    }
}
