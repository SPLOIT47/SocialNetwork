package com.feedmicroservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.feedmicroservice.datatransferobject.UserDetailsDTO;

@FeignClient(name = "usermicroservice", url = "http://localhost:8080/users")
public interface UserClient {
  
  @GetMapping("/user")
  public UserDetailsDTO getUserDetails(@RequestParam("username") String username);
}
