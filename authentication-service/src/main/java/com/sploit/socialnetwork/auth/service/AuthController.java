package com.sploit.socialnetwork.auth.service;

import com.sploit.socialnetwork.auth.exception.TokenRefreshException;
import com.sploit.socialnetwork.auth.models.RefreshToken;
import com.sploit.socialnetwork.auth.models.Role;
import com.sploit.socialnetwork.auth.models.Status;
import com.sploit.socialnetwork.auth.models.User;
import com.sploit.socialnetwork.auth.payload.request.SignInRequest;
import com.sploit.socialnetwork.auth.payload.request.SignUpRequest;
import com.sploit.socialnetwork.auth.payload.response.MessageResponse;
import com.sploit.socialnetwork.auth.payload.response.UserDetailsResponse;
import com.sploit.socialnetwork.auth.repository.RoleRepository;
import com.sploit.socialnetwork.auth.repository.UserRepository;
import com.sploit.socialnetwork.auth.security.services.RefreshTokenService;
import com.sploit.socialnetwork.auth.security.services.UserDetailsImpl;
import com.sploit.socialnetwork.auth.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthController {

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(PasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          AuthenticationManager authenticationManager,
                          JwtUtils jwtUtil, RefreshTokenService refreshTokenService) {
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
       User user = User.builder()
                .username(signUpRequest.getUsername())
                .password(encoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .build();

       Set<String> stringRoles = signUpRequest.getRoles();
       Set<Role> roles = new HashSet<>();

        if (stringRoles == null || stringRoles.isEmpty()) {
            Role role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            roles.add(role);
        } else {
            stringRoles.forEach(role -> {
                try {
                    Role existingRole = roleRepository.findByName(role)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + role));
                    roles.add(existingRole);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Role not found: " + role);
                }
            });
        }

        user.setStatus(Status.DEFAULT);
        user.setRoles(roles);
        user.setCreatedAt(Timestamp.from(Instant.now()));
        userRepository.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("User registered successfully"));
    }

    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie responseCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getRefreshToken());

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastLogin(Timestamp.from(Instant.now()));

        userRepository.save(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(UserDetailsResponse.builder()
                        .id(userDetails.getId())
                        .username(userDetails.getUsername())
                        .roles(roles)
                        .build()
                );
    }

    public ResponseEntity<?> logoutUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!Objects.equals(principal.toString(), "anonymousUser")) {
            UUID id = ((UserDetailsImpl) principal).getId();
            refreshTokenService.deleteByUserId(id);
        }

        ResponseCookie responseCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanRefreshJwtCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse("User logged out successfully"));
    }

    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUserId)
                    .map(userId -> {
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new MessageResponse("Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
    }
}
