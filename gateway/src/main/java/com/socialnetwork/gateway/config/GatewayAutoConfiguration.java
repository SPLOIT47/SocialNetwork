package com.socialnetwork.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class GatewayAutoConfiguration {

    @Value("${config.uri.auth-service}")
    private String AuthenticationServiceURI;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("authentication-service", r -> r.path("/api/auth")
                        .uri(AuthenticationServiceURI))
                .build();
    }
}

