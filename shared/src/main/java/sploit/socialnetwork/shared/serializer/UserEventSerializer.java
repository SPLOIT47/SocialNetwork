package sploit.socialnetwork.shared.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import sploit.socialnetwork.shared.dto.UserEvent;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserEventSerializer implements Serializer<UserEvent> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, UserEvent data) {
        try {
            return data == null ? null : mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize UserEvent", e);
        }
    }

    @Override
    public void close() {
    }
}
