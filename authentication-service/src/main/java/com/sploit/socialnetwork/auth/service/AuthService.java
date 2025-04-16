package com.sploit.socialnetwork.auth.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sploit.socialnetwork.auth.client.KafkaProducer;
import com.sploit.socialnetwork.auth.exception.AccessException;
import com.sploit.socialnetwork.auth.exception.RoleNotFoundException;
import com.sploit.socialnetwork.auth.exception.TokenRefreshException;
import com.sploit.socialnetwork.auth.exception.UnauthorizedException;
import com.sploit.socialnetwork.auth.exception.UserNotFoundException;
import com.sploit.socialnetwork.auth.models.RefreshToken;
import com.sploit.socialnetwork.auth.models.Role;
import com.sploit.socialnetwork.auth.models.Status;
import com.sploit.socialnetwork.auth.models.User;
import com.sploit.socialnetwork.auth.payload.event.RegisterEvent;
import com.sploit.socialnetwork.auth.payload.request.SignInRequest;
import com.sploit.socialnetwork.auth.payload.request.SignUpRequest;
import com.sploit.socialnetwork.auth.payload.response.LoginResponse;
import com.sploit.socialnetwork.auth.payload.response.LogoutResponse;
import com.sploit.socialnetwork.auth.payload.response.MessageResponse;
import com.sploit.socialnetwork.auth.payload.response.RefreshResponse;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {

    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final KafkaProducer kafkaProducer;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtil,
                       RefreshTokenService refreshTokenService,
                       KafkaProducer kafkaProducer) {
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional
    public void registerUser(SignUpRequest signUpRequest) {
       User user = User.builder()
                .password(encoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .build();

       Set<String> stringRoles = signUpRequest.getRoles();
       Set<Role> roles = new HashSet<>();

        if (stringRoles == null) {
            stringRoles = new HashSet<>();
        }

        stringRoles.forEach(role -> {
            Role existingRole = roleRepository.findByName(role)
                    .orElseThrow(() -> new RoleNotFoundException(role));
            roles.add(existingRole);
        });

        roles.add(roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER")));

        user.setStatus(Status.DEFAULT);
        user.setRoles(roles);
        user.setCreatedAt(Timestamp.from(Instant.now()));
        User savedUser = userRepository.save(user);

        RegisterEvent event = new RegisterEvent(savedUser.getId());
        kafkaProducer.sendRegisterEvent(event);
    }

    @Transactional
    public LoginResponse authenticateUser(@Valid @RequestBody SignInRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        if (user.getStatus().equals(Status.BLOCKED)) throw new AccessException(user.getId().toString());

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getId(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie responseCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getRefreshToken());

        user.setLastLogin(Timestamp.from(Instant.now()));

        userRepository.save(user);

        return LoginResponse.builder()
                .email(user.getEmail())
                .roles(roles)
                .jwtCookie(jwtCookie.toString())
                .refreshCookie(responseCookie.toString())
                .build();
    }

    @Transactional
    public LogoutResponse logoutUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ("anonymousUser".equals(principal.toString())) {
            throw new UnauthorizedException("Anonymous user");
        }

        UUID id = ((UserDetailsImpl) principal).getId();
        refreshTokenService.deleteByUserId(id);

        ResponseCookie responseCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanRefreshJwtCookie();

        return LogoutResponse.builder()
                .message("User logged out successfully")
                .cleanJwtCookie(responseCookie.toString())
                .cleanRefreshCookie(jwtRefreshCookie.toString())
                .build();
    }

    @Transactional
    public RefreshResponse refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUserId)
                    .map(userId -> {
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException("Unknown user id: ", userId.toString()));

                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
                        return RefreshResponse.builder()
                                .message("Token is refreshed successfully!")
                                .jwtCookie(jwtCookie.toString())
                                .build();
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }

        throw new TokenRefreshException("Refresh Token is empty!");
    }
}
