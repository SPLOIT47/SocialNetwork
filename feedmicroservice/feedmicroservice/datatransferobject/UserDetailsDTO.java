package com.feedmicroservice.datatransferobject;

public class UserDetailsDTO {
  private String username;
  private String password;
  private String[] authorities;
  
  public String getUsername() {
    return username;
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
  public String[] getAuthorities() {
    return authorities;
  }
  public void setAuthorities(String[] authorities) {
    this.authorities = authorities;
  }

  
}
