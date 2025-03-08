package com.sploit.socialnetwork.auth.client;

import com.sploit.socialnetwork.auth.controller.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final AuthController authController;

    @Autowired
    public KafkaConsumer(AuthController authController) {
        this.authController = authController;
    }

//    @KafkaListener(topics = "Authenticate", groupId = "base")
//    public void listen(UserEvent event) {
//        switch (event.getType()) {
//            case LOGIN -> this.authController.authenticate(null);
//            case LOGOUT -> this.authController.unauthenticate(event);
//            case REFRESH_TOKEN -> this.authController.refresh(event);
//            default -> throw new IllegalStateException("Unexpected value: " + event.getType());
//        }
//    }
}




