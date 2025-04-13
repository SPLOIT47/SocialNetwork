package com.sploit.socialnetwork.auth.client;

import com.sploit.socialnetwork.auth.payload.request.SignUpRequest;
import com.sploit.socialnetwork.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final AuthService authService;

    @Autowired
    public KafkaConsumer(AuthService authService) {
        this.authService = authService;
    }

    @KafkaListener(topics = "authentication", groupId = "register")
    public String handleRegister(SignUpRequest request) {
        return authService.registerUser(request);
    }
}




