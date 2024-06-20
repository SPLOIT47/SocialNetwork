package com.socialnetwork.feedmicroservice.config.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.datatransferobject.UserDetailsDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String keyString;

    private Key key;
    
    public JwtUtil(@Value("${jwt.secret}") String jwtSecretString) {
        byte[] decodedKey = jwtSecretString.getBytes(StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(decodedKey);
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetailsDTO userDetailsDTO) {
        final String username = extractUsername(token);
        return (username.equals(userDetailsDTO.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token, String name) {
        final String username = extractUsername(token);
        return (username.equals(name) && !isTokenExpired(token));
    }
}
