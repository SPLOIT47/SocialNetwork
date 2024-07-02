package com.socialnetworktest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.usermicroservice.Main;
import com.socialnetwork.usermicroservice.entity.UserEntity;
import com.socialnetwork.usermicroservice.service.UserService;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testRegisterUserSuccess() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("password");

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());

         Mockito.verify(userService).registerUser(any(UserEntity.class), eq("ROLE_USER"));
    }

    @Test
    public void testRegisterUserFailure_UserExists() throws Exception {
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("testuser");

        doReturn(Optional.of(existingUser)).when(userService).findByUsername("testuser");

        UserEntity newUser = new UserEntity();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        String userJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("password");

        mockMvc.perform(post("/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk());
    }

}
