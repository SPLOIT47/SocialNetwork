package com.socialnetwork.shared.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.shared.dto.UserEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class UserEventDeserializer implements Deserializer<UserEvent> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public UserEvent deserialize(String topic, byte[] data) {
        try {
            return mapper.readValue(data, UserEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize UserEvent", e);
        }
    }

    @Override
    public void close() {
    }
}