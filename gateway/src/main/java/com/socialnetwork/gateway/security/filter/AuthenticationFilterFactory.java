package com.socialnetwork.gateway.security.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilterFactory implements GatewayFilterFactory<AuthenticationFilterFactory.Config> {

    private final AuthenticationFilter authenticationFilter;

    @Autowired
    public AuthenticationFilterFactory(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return authenticationFilter;
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Override
    public Config newConfig() {
        return new Config();
    }

    @Data
    @RequiredArgsConstructor
    public static class Config {

        private String header = "Authorization";
        private boolean validateExpiration = true;

        @Value("classpath:keys/jwtSecret.key")
        private String jwtSecret;
    }
}
