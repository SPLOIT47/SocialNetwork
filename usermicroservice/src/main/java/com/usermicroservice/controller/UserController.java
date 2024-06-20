package com.usermicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.ExecutionLog;
import com.usermicroservice.config.jwt.JwtUtil;
import com.usermicroservice.entity.CustomUserDetail;
import com.usermicroservice.entity.UserEntity;
import com.usermicroservice.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired 
    UserDetailsService userDetailsService;
    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {
        try {
            userService.registerUser(user, "ROLE_USER");
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @ExecutionLog
    public ResponseEntity<String> loginUser(@RequestBody UserEntity user, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Authentication failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<String> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetail) {
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            String username = userDetail.getUsername();
            return ResponseEntity.ok("Current user: " + username);
        } else if (authentication != null && authentication.getPrincipal() instanceof String) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Anonymous user");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserEntity user) {
        try {
            userService.deleteUser(user);
            return new ResponseEntity<>("User deleted successfully ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}