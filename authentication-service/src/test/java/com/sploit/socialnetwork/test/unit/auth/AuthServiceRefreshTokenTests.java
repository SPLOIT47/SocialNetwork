package com.sploit.socialnetwork.test.unit.auth;

import com.sploit.socialnetwork.auth.client.KafkaProducer;
import com.sploit.socialnetwork.auth.exception.TokenRefreshException;
import com.sploit.socialnetwork.auth.models.RefreshToken;
import com.sploit.socialnetwork.auth.models.User;
import com.sploit.socialnetwork.auth.payload.response.RefreshResponse;
import com.sploit.socialnetwork.auth.repository.RoleRepository;
import com.sploit.socialnetwork.auth.repository.UserRepository;
import com.sploit.socialnetwork.auth.security.jwt.JwtUtils;
import com.sploit.socialnetwork.auth.security.services.RefreshTokenService;
import com.sploit.socialnetwork.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class AuthServiceRefreshTokenTests {

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

    private User fakeUser;
    private static final String FAKE_REFRESH_TOKEN = "some_refresh_token";

    @BeforeEach
    public void setUp() {
        UUID fakeId = UUID.randomUUID();
        fakeUser = User.builder()
                .id(fakeId)
                .email("email@email.com")
                .build();
    }

    @Test
    public void RefreshToken_Success_When_UserExists() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(fakeUser.getId())
                .refreshToken(FAKE_REFRESH_TOKEN)
                .expiryDate(Timestamp.from(Instant.now().plusSeconds(3600)))
                .build();

        Mockito.when(jwtUtils.getJwtRefreshFromCookies(Mockito.any(HttpServletRequest.class)))
                .thenReturn(FAKE_REFRESH_TOKEN);
        Mockito.when(refreshTokenService.findByToken(FAKE_REFRESH_TOKEN))
                .thenReturn(Optional.ofNullable(refreshToken));
        Mockito.when(refreshTokenService.verifyExpiration(Mockito.any(RefreshToken.class)))
                .thenReturn(refreshToken);
        Mockito.when(userRepository.findById(fakeUser.getId())).thenReturn(Optional.of(fakeUser));
        ResponseCookie mockResponseCookie = Mockito.mock(ResponseCookie.class);
        Mockito.when(jwtUtils.generateJwtCookie(Mockito.any(User.class)))
                .thenReturn(mockResponseCookie);
        Mockito.when(mockResponseCookie.toString()).thenReturn(FAKE_REFRESH_TOKEN);

        RefreshResponse response = authService.refreshToken(httpServletRequest);

        assertEquals("Token is refreshed successfully!", response.getMessage());
        assertEquals(FAKE_REFRESH_TOKEN, response.getJwtCookie());
    }

    @Test
    public void RefreshToken_Failure_When_RefreshDoesNotExist() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(fakeUser.getId())
                .refreshToken(FAKE_REFRESH_TOKEN)
                .expiryDate(Timestamp.from(Instant.now().plusSeconds(3600)))
                .build();

        Mockito.lenient().when(jwtUtils.getJwtRefreshFromCookies(Mockito.any(HttpServletRequest.class)))
                .thenReturn("");


        assertThrows(TokenRefreshException.class, () -> authService.refreshToken(httpServletRequest));
    }

    @Test
    public void RefreshToken_Failure_When_RefreshTokenIsEmpty() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);



        assertThrows(TokenRefreshException.class, () -> authService.refreshToken(httpServletRequest));
    }

    @Test
    public void RefreshToken_Failure_When_RefreshTokenIsNull() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

        Mockito.lenient().when(jwtUtils.getJwtRefreshFromCookies(Mockito.any(HttpServletRequest.class)))
                .thenReturn(null);

        assertThrows(TokenRefreshException.class, () -> authService.refreshToken(httpServletRequest));
    }
}
