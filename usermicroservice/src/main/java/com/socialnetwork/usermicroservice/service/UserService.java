package com.socialnetwork.usermicroservice.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.annotation.ExecutionLog;
import com.datatransferobject.UserDetailsDTO;
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
  
  @ExecutionLog
  public UserEntity registerUser(UserEntity user, String roleTag) throws Exception {
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

    return userRepository.save(user);
  }

  public Optional<UserEntity> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Transactional
  @ExecutionLog
  public void deleteUser(UserEntity user) throws Exception {
    UserEntity existingUser = userRepository.findByUsername(user.getUsername())
      .orElseThrow(() -> new Exception("Wrong username or password"));

    if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
      throw new Exception("Wrong username or password");
    }

    existingUser.getRoles().forEach(role -> role.getUsers().remove(existingUser));
    existingUser.getRoles().clear();

    userRepository.deleteById(existingUser.getId());
  }
  
  @ExecutionLog
  public String authenticateUser(UserEntity user) throws Exception {
    try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return jwtUtil.generateToken(userDetails);
    } catch (Exception e) {
        throw new Exception("Invalid username or password", e);
    }
}

  @ExecutionLog
  public HttpStatus validateToken(UserDetailsDTO object) {
    try {
        String extractedUsername = jwtUtil.extractUsername(object.getToken());
        if (!extractedUsername.equals(object.getUsername())) {
            return HttpStatus.UNAUTHORIZED;
        }
        return HttpStatus.OK;
    } catch (Exception e) {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }
}
