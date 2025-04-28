package com.sploit.socialnetwork.auth.security.jwt;

import com.sploit.socialnetwork.auth.models.User;
import com.sploit.socialnetwork.auth.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.jwt-secret-location}")
    private String jwtSecretLocation;

    @Value("${jwt.expiration-millis}")
    private Long jwtExpirationMs;

    @Value("${jwt.cookie-name}")
    private String jwtCookieName;

    @Value("${jwt.refresh-cookie-name}")
    private String jwtRefreshTokenName;

    private final ResourceLoader resourceLoader;

    @Autowired
    public JwtUtils(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromId(userPrincipal.getUsername());
        return generateCookie(jwtCookieName, jwt, "/api");
    }

    public ResponseCookie generateJwtCookie(User user) {
        String jwt = generateTokenFromId(user.getId().toString());
        return generateCookie(jwtCookieName, jwt, "/api");
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(jwtRefreshTokenName, refreshToken, "/api/auth/refresh");
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshTokenName);
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookieName);
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName).path("/api").build();
    }

    public ResponseCookie getCleanRefreshJwtCookie() {
        return ResponseCookie.from(jwtRefreshTokenName).path("/api/auth/refresh").build();
    }

    public String getIdFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private Key key() {
        Resource resource = resourceLoader.getResource(jwtSecretLocation);
        String jwtSecret;
        try (InputStream is = resource.getInputStream()) {
            jwtSecret = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (jwtSecret.isEmpty()) {
            throw new RuntimeException("JWT Secret could not be found");
        }

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateTokenFromId(String id) {
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie.from(name, value).path(path).build();
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }
}
