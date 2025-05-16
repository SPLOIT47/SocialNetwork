package com.socialnetwork.gateway.config;

import com.socialnetwork.gateway.security.filter.AuthenticationFilterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayAutoConfiguration {

    @Value("${config.uri.auth-service}")
    private String AuthenticationServiceURI;

    private final AuthenticationFilterFactory authenticationFilterFactory;

    @Autowired
    public GatewayAutoConfiguration(AuthenticationFilterFactory authenticationFilterFactory) {
        this.authenticationFilterFactory = authenticationFilterFactory;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("authentication-service", r -> r.path("/api/auth/**")
                        .uri(AuthenticationServiceURI))
                .build();
    }
}
