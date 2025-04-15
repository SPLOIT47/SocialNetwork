package com.sploit.socialnetwork.auth.config;

import com.sploit.socialnetwork.auth.payload.request.SignInRequest;
import com.sploit.socialnetwork.auth.payload.request.SignUpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, SignUpRequest> signUpRequestConsumerFactory() {
        return createDefaultKafkaConsumerFactory("register", SignUpRequest.class);
    }

    @Bean ConsumerFactory<String, SignInRequest> signInRequestConsumerFactory() {
        return createDefaultKafkaConsumerFactory("login", SignInRequest.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SignUpRequest> signUpRequestKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SignUpRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(signUpRequestConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SignInRequest> signInRequestKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SignInRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(signInRequestConsumerFactory());
        return factory;
    }

    private <ValueDefaultType> DefaultKafkaConsumerFactory<String, ValueDefaultType>  createDefaultKafkaConsumerFactory(
            String groupId,
            Class<ValueDefaultType> valueDefaultTypeClass) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueDefaultTypeClass);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.sploit.socialnetwork.auth.payload.request");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(config);
    }
}
