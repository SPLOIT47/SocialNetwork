package com.socialnetwork.feedmicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.feedmicroservice.body.PublishFeedBody;
import com.socialnetwork.feedmicroservice.config.jwt.JwtUtil;

@RestController
@RequestMapping("/feed")
public class FeedController {
  @Autowired
  JwtUtil jwtUtil;

  @PostMapping("/publish")
  public ResponseEntity<?> publish(@RequestBody PublishFeedBody body) {
    if (jwtUtil.validateToken(body.token(), body.username())) {
      return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }
}
