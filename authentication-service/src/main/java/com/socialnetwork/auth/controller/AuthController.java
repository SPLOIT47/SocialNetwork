package com.socialnetwork.auth.controller;

import com.socialnetwork.auth.dto.JwtAuthenticationResponse;
import com.socialnetwork.auth.dto.SignInRequest;
import com.socialnetwork.shared.dto.UserEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticate(@RequestBody SignInRequest request) {
        var response = new JwtAuthenticationResponse();
        response.setToken("token");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> unauthenticate(UserEvent user) {
        return null;
    }

    public ResponseEntity<?> refresh(UserEvent user) {
        return null;
    }
}
