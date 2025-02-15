package com.sploit.socialnetwork.auth.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import sploit.socialnetwork.shared.dto.UserEvent;
import sploit.socialnetwork.shared.serializer.UserEventDeserializer;
import sploit.socialnetwork.shared.serializer.UserEventSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConfiguration {

    @Bean
    public ProducerFactory<String, UserEvent> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        // FIXME: use @Value for host && port
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserEventSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, UserEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, UserEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new UserEventDeserializer());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> config = new HashMap<>();
        // FIXME: use @Value for host && port
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserEventDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "base");
        return config;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, UserEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, UserEvent> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, UserEvent> messageListenerContainer() {
        ContainerProperties containerProperties = new ContainerProperties("Authenticate");
        ConcurrentMessageListenerContainer<String, UserEvent> container =
                new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProperties);

        container.setupMessageListener((MessageListener<String, UserEvent>) record -> {
            log.info("Received message: {}", record.value());
        });

        return container;
    }
}
