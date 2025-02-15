package com.sploit.socialnetwork.auth.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sploit.socialnetwork.shared.dto.UserEvent;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, UserEvent message) {
        kafkaTemplate.send(topic, message);
    }
}
