package com.usermicroservice.entity;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
public class UserEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  UUID id;

  @Column(unique = true, nullable = false)
  String username;
  
  public UserEntity() {
  }

  @Column(nullable = false)
  String password;

  @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "user_roles", 
      joinColumns = @JoinColumn(name = "user_id"), 
      inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public Set<RoleEntity> getRoles() {
    return roles;
  }

  public void setRoles(Set<RoleEntity> roles) {
    this.roles = roles;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  
}
