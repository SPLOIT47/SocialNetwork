package com.socialnetwork.gateway.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;

public class JwtUtils {

    @Value("classpath:keys/jwtSecret.key")
    private String jwtSecret;

    public void validate(String token) {
        Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                .build().parseClaimsJws(token);
    }
}
