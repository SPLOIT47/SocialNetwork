package com.sploit.socialnetwork.auth.controller;

import com.sploit.socialnetwork.auth.dto.SignInRequest;
import com.sploit.socialnetwork.auth.payload.request.SignUpRequest;
import com.sploit.socialnetwork.auth.payload.response.MessageResponse;
import com.sploit.socialnetwork.auth.repository.RefreshTokenRepository;
import com.sploit.socialnetwork.auth.repository.RoleRepository;
import com.sploit.socialnetwork.auth.repository.UserRepository;
import com.sploit.socialnetwork.auth.security.services.UserDetailsImpl;
import com.sploit.socialnetwork.auth.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import sploit.socialnetwork.shared.exception.InvalidRoleException;
import sploit.socialnetwork.shared.models.ERole;
import sploit.socialnetwork.shared.models.Role;
import sploit.socialnetwork.shared.models.User;

import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtil;

    @Autowired
    public AuthController(PasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          RefreshTokenRepository refreshTokenRepository,
                          RoleRepository roleRepository,
                          AuthenticationManager authenticationManager,
                          JwtUtils jwtUtil) {
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .password(encoder.encode(signUpRequest.getPassword()))
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .build();

        Set<String> stringRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (stringRoles == null) {
            Role role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            roles.add(role);
        } else {
            stringRoles.forEach(role -> {
                try {
                    ERole eRole = ERole.valueOf(role);
                    Role rRole = roleRepository.findByName(eRole.toString())
                            .orElseThrow(() -> new RuntimeException("Role not found: " + eRole.toString()));
                    roles.add(rRole);
                } catch (IllegalArgumentException e) {
                    throw new InvalidRoleException(role);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody SignInRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), encoder.encode(request.getPassword())));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCockie;
        return null;
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        return null;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
        return null;
    }
}
