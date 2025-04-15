package com.sploit.socialnetwork.auth.controller;

import com.sploit.socialnetwork.auth.payload.request.SignInRequest;
import com.sploit.socialnetwork.auth.payload.request.SignUpRequest;
import com.sploit.socialnetwork.auth.payload.response.LoginResponse;
import com.sploit.socialnetwork.auth.payload.response.LogoutResponse;
import com.sploit.socialnetwork.auth.payload.response.RefreshResponse;
import com.sploit.socialnetwork.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid SignUpRequest signUpRequest) {
        authService.registerUser(signUpRequest);

        return ResponseEntity.ok("User registered!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid SignInRequest signInRequest) {
        LoginResponse response = authService.authenticateUser(signInRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,response.getJwtCookie())
                .header(HttpHeaders.SET_COOKIE, response.getRefreshCookie())
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout() {
        LogoutResponse response = authService.logoutUser();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getCleanJwtCookie())
                .header(HttpHeaders.SET_COOKIE, response.getCleanRefreshCookie())
                .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(HttpServletRequest request) {
        RefreshResponse response = authService.refreshToken(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getJwtCookie())
                .body(response);
    }
}
