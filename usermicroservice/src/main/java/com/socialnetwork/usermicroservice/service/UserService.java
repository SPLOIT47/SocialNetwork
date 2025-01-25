package com.socialnetwork.usermicroservice.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socialnetwork.usermicroservice.config.jwt.JwtUtil;
import com.socialnetwork.usermicroservice.entity.RoleEntity;
import com.socialnetwork.usermicroservice.entity.UserEntity;
import com.socialnetwork.usermicroservice.repository.RoleRepository;
import com.socialnetwork.usermicroservice.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    public void registerUser(UserEntity user, String roleTag) throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        RoleEntity role = roleRepository.findByName(roleTag);

        if (role == null) {
            throw new Exception("Role " + roleTag + " not found.");
        }

        Set<RoleEntity> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);
    }

    public Optional<UserEntity> findByLogin(String login) {
        return userRepository.findByNickname(login);
    }

    @Transactional
    public void deleteUser(UserEntity user) throws Exception {
        UserEntity existingUser = userRepository.findByNickname(user.getNickname())
                .orElseThrow(() -> new Exception("Wrong username or password"));

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new Exception("Wrong username or password");
        }

        existingUser.getRoles().forEach(role -> role.getUsers().remove(existingUser));
        existingUser.getRoles().clear();

        userRepository.deleteById(existingUser.getId());
    }

    public String authenticateUser(UserEntity user) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            user.getNickname(), user.getPassword()));
            final org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername(user.getNickname());
            return jwtUtil.generateToken(userDetails);
        } catch (Exception e) {
            throw new Exception("Invalid username or password", e);
        }
    }

    @Transactional
    public void changeUserData(UserEntity user, Map<String, String> userWithNewData) {
        UserEntity existingUser = userRepository.findByNickname(user.getNickname()).orElseThrow();
        if (userWithNewData.containsKey("firstName") && userWithNewData.get("firstName") != null) {
            existingUser.setFirstName(userWithNewData.get("firstName"));
        }
        if (userWithNewData.containsKey("lastName") && userWithNewData.get("lastName") != null) {
            existingUser.setLastName(userWithNewData.get("lastName"));
        }
        if (userWithNewData.containsKey("email") && userWithNewData.get("email") != null) {
            existingUser.setEmail(userWithNewData.get("email"));
        }
        if (userWithNewData.containsKey("age") && userWithNewData.get("age") != null) {
            existingUser.setAge(userWithNewData.get("age"));
        }
        if (userWithNewData.containsKey("phoneNumber")
                && userWithNewData.get("phoneNumber") != null) {
            existingUser.setPhoneNumber(userWithNewData.get("phoneNumber"));
        }

        userRepository.save(existingUser);
    }


    public void  changeLogin(UserEntity user, String login) throws Exception {
        if (userRepository.findByNickname(login).isPresent()) {
            throw new Exception("Login is already in use");
        }

        UserEntity existingUser = userRepository.findByNickname(user.getNickname()).orElseThrow();
        existingUser.setNickname(login);
        userRepository.save(existingUser);
    }

    public void changePassword(UserEntity user, String newPassword) {
        UserEntity existingUser = userRepository.findByNickname(user.getNickname()).orElseThrow();
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
    }

    public UserEntity getUserInfo(String id) {
        return userRepository.findById(id).orElseThrow();
    }

    public List<UserEntity> fingAllUsers() {
        Iterable<UserEntity> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false).toList();
    }
}