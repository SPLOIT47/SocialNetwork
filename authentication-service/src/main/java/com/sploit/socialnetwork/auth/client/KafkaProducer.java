package com.sploit.socialnetwork.auth.client;

import com.sploit.socialnetwork.auth.payload.event.RegisterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String REGISTER_TOPIC = "user.registered";
    @Autowired
    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRegisterEvent(RegisterEvent registerEvent) {
        kafkaTemplate.send(REGISTER_TOPIC, registerEvent);
    }
}
