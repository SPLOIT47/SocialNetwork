package com.sploit.socialnetwork.test.auth;

import com.sploit.socialnetwork.auth.client.KafkaProducer;
import com.sploit.socialnetwork.auth.exception.UnauthorizedException;
import com.sploit.socialnetwork.auth.payload.response.LogoutResponse;
import com.sploit.socialnetwork.auth.repository.RoleRepository;
import com.sploit.socialnetwork.auth.repository.UserRepository;
import com.sploit.socialnetwork.auth.security.jwt.JwtUtils;
import com.sploit.socialnetwork.auth.security.services.RefreshTokenService;
import com.sploit.socialnetwork.auth.security.services.UserDetailsImpl;
import com.sploit.socialnetwork.auth.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceLogoutTests {

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

    private UUID fakeId;
    private Authentication mockAuthentication;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @BeforeEach
    void setUp() {
        fakeId = UUID.randomUUID();
        UserDetailsImpl mockUserDetails = Mockito.mock(UserDetailsImpl.class);
        Mockito.lenient().when(mockUserDetails.getId()).thenReturn(fakeId);

        mockAuthentication = Mockito.mock(Authentication.class);
        Mockito.lenient().when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);

        SecurityContext mockSecurityContext = Mockito.mock(SecurityContext.class);

        SecurityContextHolder.setContext(mockSecurityContext);

        Mockito.lenient().when(jwtUtils.getCleanJwtCookie()).thenReturn(ResponseCookie.from("jwt", "").build());
        Mockito.lenient().when(jwtUtils.getCleanRefreshJwtCookie()).thenReturn(ResponseCookie.from("refresh", "").build());

    }

    @Test
    public void LogoutUser_Success_UserAuthenticated() {
        SecurityContext mockSecurityContext = SecurityContextHolder.getContext();
        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        LogoutResponse response = authService.logoutUser();
        assertEquals("User logged out successfully", response.getMessage());
        verify(refreshTokenService, times(1)).deleteByUserId(fakeId);
    }

    @Test
    public void LogoutUser_Failed_UserNotAuthenticated() {
        SecurityContext mockSecurityContext = SecurityContextHolder.getContext();
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(
                "key",
                "anonymousUser",
                List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
        );
        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(auth);

        assertThrows(UnauthorizedException.class, () -> authService.logoutUser());
    }
}
