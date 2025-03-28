package com.sploit.socialnetwork.auth.client;

import com.sploit.socialnetwork.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final AuthService authService;

    @Autowired
    public KafkaConsumer(AuthService authService) {
        this.authService = authService;
    }

}




