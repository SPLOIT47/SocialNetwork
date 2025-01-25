package com.socialnetwork.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.spec.PKCS8EncodedKeySpec;


@Configuration
@Slf4j
public class JwtSecurityConfiguration {

    @Value("${jwt.public-key-location}")
    private String publicKeyLocation;

    @Value("${jwt.private-key-location}")
    private String privateKeyLocation;

    @Autowired // do not use field injection
    private ResourceLoader resourceLoader;

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        RSAPrivateKey privateKey = loadPrivateKey();
        RSAPublicKey publicKey = loadPublicKey();
        return new NimbusJwtEncoder((jwkSelector, context) -> jwkSelector.select(new JWKSet(
                new RSAKey.Builder(publicKey).privateKey(privateKey).build()
        )));
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        RSAPublicKey publicKey = loadPublicKey();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    private RSAPrivateKey loadPrivateKey() throws Exception {
        Resource resource = resourceLoader.getResource(privateKeyLocation);
        String val;
        try (InputStream inputStream = resource.getInputStream()) {
            val = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        String key = new String(val)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    private RSAPublicKey loadPublicKey() throws Exception {
        Resource resource = resourceLoader.getResource(publicKeyLocation);
        String val;
        try (InputStream inputStream = resource.getInputStream()) {
            val = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        String key = new String(val)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }
}
