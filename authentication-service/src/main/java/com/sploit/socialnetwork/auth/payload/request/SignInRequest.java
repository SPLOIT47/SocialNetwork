package com.sploit.socialnetwork.auth.payload.request;

import com.sploit.socialnetwork.auth.annotation.OneNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@OneNotBlank(fields = {"username", "email"}, message = "username or email shouldn't be empty")
public class SignInRequest {
    private String username;
    private String email;

    @NotBlank
    private String password;
}
