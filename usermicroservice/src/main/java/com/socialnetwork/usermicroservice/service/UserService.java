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
  public UserEntity registerUser(UserEntity user, String roleTag) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    
    RoleEntity role = roleRepository.findByName(roleTag);

    Set<RoleEntity> roles = user.getRoles();
    if (roles == null) {
      roles = new HashSet<>();
    }
    roles.add(role);
    user.setRoles(roles);

    return userRepository.save(user);
  }

  @ExecutionLog
  public void deleteUser(UserEntity user) throws Exception {
    if (!compareLogin(user)) {
      throw new Exception("Wrong username or password");
    }
    UserEntity existingUser = userRepository.findByUsername(user.getUsername()).get();
    userRepository.deleteById(existingUser.getId());
  }

  private boolean compareLogin(UserEntity user) {
    Optional<UserEntity> existingOptionalUser = userRepository.findByUsername(user.getUsername());
    if (!existingOptionalUser.isPresent()) {
      return false;
    }
    UserEntity existingUser = existingOptionalUser.get();
    return passwordEncoder.matches(user.getPassword(), existingUser.getPassword());
  }

  @ExecutionLog
  public String authenticateUser(UserEntity user) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
    return jwtUtil.generateToken(userDetails);
  }

  @ExecutionLog
  public HttpStatus validateToken(UserDetailsDTO object) {
    try {
      String extractedUsername = jwtUtil.extractUsername();
      if (!extractedUsername.equals(username)) {
        return HttpStatus.BAD_REQUEST;
      }
      return HttpStatus.OK;
    } catch (Exception e) {
      return HttpStatus.BAD_REQUEST;
    }

  } 
}
