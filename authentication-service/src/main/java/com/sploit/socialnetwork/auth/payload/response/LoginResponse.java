package com.sploit.socialnetwork.auth.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private transient String jwtCookie;

    @JsonIgnore
    private transient String refreshCookie;
}
