package com.sploit.socialnetwork.test.unit.auth;

import com.sploit.socialnetwork.auth.client.KafkaProducer;
import com.sploit.socialnetwork.auth.exception.AccessException;
import com.sploit.socialnetwork.auth.models.RefreshToken;
import com.sploit.socialnetwork.auth.models.Role;
import com.sploit.socialnetwork.auth.models.Status;
import com.sploit.socialnetwork.auth.models.User;
import com.sploit.socialnetwork.auth.payload.event.RegisterEvent;
import com.sploit.socialnetwork.auth.payload.request.SignInRequest;
import com.sploit.socialnetwork.auth.payload.response.LoginResponse;
import com.sploit.socialnetwork.auth.repository.RoleRepository;
import com.sploit.socialnetwork.auth.repository.UserRepository;
import com.sploit.socialnetwork.auth.security.jwt.JwtUtils;
import com.sploit.socialnetwork.auth.security.services.RefreshTokenService;
import com.sploit.socialnetwork.auth.security.services.UserDetailsImpl;
import com.sploit.socialnetwork.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceAuthenticationTests {

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private KafkaProducer kafkaProducer;

    private User testUser;

    @BeforeEach
    public void setUp() {
        Mockito.lenient().when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(Optional.of(new Role("ROLE_USER")));
        Mockito.lenient().when(roleRepository.findByName("ROLE_ADMIN"))
                .thenReturn(Optional.of(new Role("ROLE_ADMIN")));
        Mockito.lenient().when(roleRepository.findByName("ROLE_MODERATOR"))
                .thenReturn(Optional.of(new Role("ROLE_MODERATOR")));



        UUID testId = UUID.randomUUID();

        testUser = User.builder()
                .id(testId)
                .password("encodedPassword")
                .email("email@email.com")
                .roles(Set.of(new Role("ROLE_USER")))
                .status(Status.DEFAULT)
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        Mockito.lenient().when(userRepository.findByEmail(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String email = invocation.getArgument(0, String.class);
                    if ("email@email.com".equals(email)) {
                        return Optional.ofNullable(testUser);
                    }
                    throw new IllegalArgumentException("Unexpected email: " + email);
                });

        UserDetailsImpl userDetails = new UserDetailsImpl(
                testUser.getId(),
                testUser.getPassword(),
                testUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );

        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        Mockito.lenient().when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        Mockito.lenient().when(mockAuthentication.getPrincipal()).thenReturn(userDetails);

        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID())
                .refreshToken("mockRefreshTokenValue")
                .expiryDate(Instant.ofEpochSecond(10))
                .build();

        Mockito.lenient().when(refreshTokenService.createRefreshToken(Mockito.any(UUID.class))).thenReturn(refreshToken);

        ResponseCookie mockResponseCookie = Mockito.mock(ResponseCookie.class);

        Mockito.lenient().when(jwtUtils.generateRefreshJwtCookie(Mockito.any(String.class))).thenReturn(mockResponseCookie);
        Mockito.lenient().when(jwtUtils.generateJwtCookie(Mockito.any(UserDetailsImpl.class))).thenReturn(mockResponseCookie);

        Mockito.lenient().doNothing().when(kafkaProducer).sendRegisterEvent(Mockito.any(RegisterEvent.class));

    }

    @Test
    public void AuthenticateUser_Success_IfRequestValidAndUserExists() {
        SignInRequest request = SignInRequest.builder()
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .build();
        LoginResponse response = authService.authenticateUser(request);

        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
    }

    @Test
    public void AuthenticateUser_Failed_IfEmailNotExists() {
        SignInRequest request = SignInRequest.builder()
                .email("email_that_not_exists@email.com")
                .password("password")
                .build();

        assertThrows(IllegalArgumentException.class, () -> authService.authenticateUser(request));
    }

    @Test
    public void AuthenticateUser_Failed_IfPasswordNotMatch() {
        Mockito.reset(authenticationManager);

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenAnswer(invocation -> {
                    UsernamePasswordAuthenticationToken token = invocation.getArgument(0);
                    String password = token.getCredentials().toString();
                    if (!"encodedPassword".equals(password)) {
                        throw new BadCredentialsException("Invalid credentials");
                    }
                    return authenticationManager;
                });

        SignInRequest request = SignInRequest.builder()
                .email("email@email.com")
                .password("password_that_not_match")
                .build();

        assertThrows(BadCredentialsException.class, () -> authService.authenticateUser(request));
    }

    @Test
    public void AuthenticateUser_Failed_IfStatusBlock() {
        Mockito.reset(userRepository);

        testUser.setStatus(Status.BLOCKED);

        Mockito.when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.of(testUser));

        SignInRequest request = SignInRequest.builder()
                .email("email@email.com")
                .password("password_that_not_match")
                .build();

        assertThrows(AccessException.class, () -> authService.authenticateUser(request));
    }
}
