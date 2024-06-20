package com.socialnetwork.feedmicroservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
            
            UserDetailsDTO response = restTemplate.getForObject(builder.toUriString(), UserDetailsDTO.class);
            return response;
        } catch (RestClientException e) {
            logger.error("Error occurred while validating token: ", e);
            return null;
        }
    }
}
