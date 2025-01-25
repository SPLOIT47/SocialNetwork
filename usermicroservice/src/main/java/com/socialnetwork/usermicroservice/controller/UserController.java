package com.socialnetwork.usermicroservice.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.socialnetwork.deps.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;  // TODO: fix this import

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
            Optional<UserEntity> existingUser = userService.findByLogin(user.getNickname());
            if (existingUser.isPresent()) {
                return new ResponseEntity<>("Registration failed: User already exists",
                        HttpStatus.BAD_REQUEST);
            }
            userService.registerUser(user, "ROLE_USER");
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserEntity user) {
        try {
            final String jwt = userService.authenticateUser(user);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Authentication failed: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserEntity user) {
        try {
            userService.deleteUser(user);
            return new ResponseEntity<>("User deleted successfully ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changeUserInfo")
    public ResponseEntity<String> changeUserData(@RequestBody Map<String, String> newData) {
        try {
            if (newData.get("nickname") == null) {
                return new ResponseEntity<>("Login required", HttpStatus.BAD_REQUEST);
            }

            UserEntity existingUser =  userService.findByLogin(newData.get("nickname"))
                    .orElseThrow();
            //userService.ChangeUserData(existingUser, newData);
            return  new ResponseEntity<>("Info changed successfully ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to change Info: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changeUserLogin")
    public ResponseEntity<String> changeUserLogin(
            @RequestParam String newLogin, @RequestBody UserEntity user) {
        try {
            userService.changeLogin(user, newLogin);
            return new ResponseEntity<>("Login changed successfully ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to change login: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changeUserPassword")
    public ResponseEntity<String> changeUserPassword(
            @RequestParam String newPassword, @RequestBody UserEntity user) {
        try {
            userService.changePassword(user, newPassword);
            return new ResponseEntity<>("Password changed successfully ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to change password: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<UserEntity> getUserInfo(@RequestParam String id) {
        try {
            UserEntity userInfo = userService.getUserInfo(id);
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = userService.fingAllUsers()
                .stream()
                .map(user -> new UserDTO(
                        UUID.fromString(user.getId()),
                        user.getFirstName(),
                        user.getLastName(),
                        null))
                .toList();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}