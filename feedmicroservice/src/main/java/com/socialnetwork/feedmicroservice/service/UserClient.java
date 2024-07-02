package com.socialnetwork.feedmicroservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.datatransferobject.UserDetailsDTO;

@Service
public class UserClient {

    private static final Logger logger = LoggerFactory.getLogger(UserClient.class);

    @Autowired
    RestTemplate restTemplate;

    private final String USER_SERVICE_URL = "http://localhost:8080/users/validateToken";

    public UserDetailsDTO validateToken(String token, String username) {
        try {
            logger.info("Sending request to validate token to " + USER_SERVICE_URL);
            
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(USER_SERVICE_URL)
                .queryParam("token", token)
                .queryParam("username", username);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<UserDetailsDTO> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, UserDetailsDTO.class);
            logger.info("Got response");
            return response.getBody();
        } catch (RestClientException e) {
            logger.error("Error occurred while validating token: ", e);
            return null;
        }
    }
}
