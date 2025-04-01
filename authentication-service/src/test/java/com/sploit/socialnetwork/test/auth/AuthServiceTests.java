package com.sploit.socialnetwork.test.auth;

import com.sploit.socialnetwork.auth.models.Role;
import com.sploit.socialnetwork.auth.payload.request.SignUpRequest;
import com.sploit.socialnetwork.auth.repository.RoleRepository;
import com.sploit.socialnetwork.auth.repository.UserRepository;
import com.sploit.socialnetwork.auth.security.jwt.JwtUtils;
import com.sploit.socialnetwork.auth.security.services.RefreshTokenService;
import com.sploit.socialnetwork.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

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

    @BeforeEach
    public void setUp() {
        Mockito.lenient().when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(Optional.of(new Role("ROLE_USER")));
        Mockito.lenient().when(roleRepository.findByName("ROLE_ADMIN"))
                .thenReturn(Optional.of(new Role("ROLE_ADMIN")));
        Mockito.lenient().when(roleRepository.findByName("ROLE_MODERATOR"))
                .thenReturn(Optional.of(new Role("ROLE_MODERATOR")));
    }

    @Test
    public void RegisterUser_Success_IfRequestValidWithNoRoles() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("test");
        signUpRequest.setPassword("password");
        signUpRequest.setEmail("test@test.com");

        ResponseEntity<?> response = authService.registerUser(signUpRequest);

        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    public void RegisterUser_Success_IfRequestValidWithRoleAdminWithoutRoleUser() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("test");
        signUpRequest.setPassword("password");
        signUpRequest.setEmail("test@test.com");

        signUpRequest.setRoles(roles);

        ResponseEntity<?> response = authService.registerUser(signUpRequest);

        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    public void RegisterUser_Success_IfRequestValidWithRoleAdminWithRoleUser() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("test");
        signUpRequest.setPassword("password");
        signUpRequest.setEmail("test@test.com");

        signUpRequest.setRoles(roles);

        ResponseEntity<?> response = authService.registerUser(signUpRequest);

        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals("User registered successfully", response.getBody());
    }


    @Test
    public void RegisterUser_Success_IfRequestValidWithRoleModeratorWithoutRoleUser() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_MODERATOR");

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("test");
        signUpRequest.setPassword("password");
        signUpRequest.setEmail("test@test.com");

        signUpRequest.setRoles(roles);

        ResponseEntity<?> response = authService.registerUser(signUpRequest);

        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    public void RegisterUser_Success_IfRequestValidWithRoleModeratorWithRoleUser() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_MODERATOR");
        roles.add("ROLE_USER");

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("test");
        signUpRequest.setPassword("password");
        signUpRequest.setEmail("test@test.com");

        signUpRequest.setRoles(roles);

        ResponseEntity<?> response = authService.registerUser(signUpRequest);

        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    public void RegisterUser_Success_IfRequestValidWithNoExistingRole() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_SOME_NOT_CORRECT_ROLE");

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("test");
        signUpRequest.setPassword("password");
        signUpRequest.setEmail("test@test.com");

        signUpRequest.setRoles(roles);

        assertThrows(RuntimeException.class, () -> authService.registerUser(signUpRequest));
    }
}
