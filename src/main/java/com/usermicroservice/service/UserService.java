package com.usermicroservice.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.annotation.ExecutionLog;
import com.usermicroservice.entity.RoleEntity;
import com.usermicroservice.entity.UserEntity;
import com.usermicroservice.repository.RoleRepository;
import com.usermicroservice.repository.UserRepository;

@Service
public class UserService {
  
  @Autowired 
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

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
}
