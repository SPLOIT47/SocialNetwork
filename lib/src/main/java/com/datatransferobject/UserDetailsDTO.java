package com.datatransferobject;


public class UserDetailsDTO {
    private String username;
    private String password;
    private String[] authorities;
    private String token;
    private String firstname;
    private String secondname;
    private String emnail;
    private String phone;
    
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
    public String getToken() {
      return token;
    }
    public void setToken(String token) {
      this.token = token;
    }
    public String getFirstname() {
      return firstname;
    }
    public void setFirstname(String firstname) {
      this.firstname = firstname;
    }
    public String getSecondname() {
      return secondname;
    }
    public void setSecondname(String secondname) {
      this.secondname = secondname;
    }
    public String getEmnail() {
      return emnail;
    }
    public void setEmnail(String emnail) {
      this.emnail = emnail;
    }
    public String getPhone() {
      return phone;
    }
    public void setPhone(String phone) {
      this.phone = phone;
    }
    
  }
  
