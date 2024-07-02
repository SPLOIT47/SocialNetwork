package com.socialnetwork.usermicroservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.ExecutionLog;
import com.datatransferobject.UserDetailsDTO;
import com.socialnetwork.usermicroservice.entity.CustomUserDetail;
import com.socialnetwork.usermicroservice.entity.UserEntity;
import com.socialnetwork.usermicroservice.service.UserService;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {
        try {
            Optional<UserEntity> existingUser = userService.findByUsername(user.getUsername());
            if (existingUser.isPresent()) {
                return new ResponseEntity<>("Registration failed: User already exists", HttpStatus.BAD_REQUEST);
            }        
            userService.registerUser(user, "ROLE_USER");
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserEntity user) {
        try {
            final String jwt = userService.authenticateUser(user);
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
    @ExecutionLog
    public ResponseEntity<String> deleteUser(@RequestBody UserEntity user) {
        try {
            userService.deleteUser(user);
            return new ResponseEntity<>("User deleted successfully ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/validateToken")
        public ResponseEntity<UserDetailsDTO> validateToken(@RequestParam String token, @RequestParam String username) {
        UserDetailsDTO object = new UserDetailsDTO();
        object.setToken(token);
        object.setUsername(username);
        HttpStatus status = userService.validateToken(object);
        return ResponseEntity.status(status).body(object);
    }
}