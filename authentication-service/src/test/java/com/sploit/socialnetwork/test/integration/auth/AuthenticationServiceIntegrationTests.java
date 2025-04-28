package com.sploit.socialnetwork.test.integration.auth;

import com.sploit.socialnetwork.auth.App;
import com.sploit.socialnetwork.auth.client.KafkaProducer;
import com.sploit.socialnetwork.auth.payload.request.SignUpRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationServiceIntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("authTestDb")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7.4")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void config(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
    }

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    KafkaProducer kafkaProducer;

    @ParameterizedTest
    @Order(1)
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    @Execution(ExecutionMode.SAME_THREAD)
    void shouldRegisterUsers_WhenUsersIsNotRegistered(int i) {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test" + i + "@gmail.com");
        signUpRequest.setPassword("test" + i);

        String url = "http://localhost:" + port + "/api/auth/register";
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                url, signUpRequest, String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Ошибка регистрации: " + response.getBody());
        }

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
