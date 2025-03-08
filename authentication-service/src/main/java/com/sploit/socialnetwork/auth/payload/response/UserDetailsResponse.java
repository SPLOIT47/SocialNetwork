package com.sploit.socialnetwork.auth.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserDetailsResponse {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private int age;
}
