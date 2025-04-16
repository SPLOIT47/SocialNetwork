package com.sploit.socialnetwork.auth.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String email;
    private List<String> roles;
    private transient String jwtCookie;
    private transient String refreshCookie;
}
