package com.socialnetwork.auth.util;

import com.nimbusds.jwt.JWT;
import com.socialnetwork.shared.dto.UserEvent;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;

public class JwtUtil {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtUtil(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(UserEvent user) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("social-network")
                .subject(user.getId().toString())
                .claim(JwtClaimNames.EXP, Instant.now().plusSeconds(604800))
                .build();

        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(header, claims);

        return jwtEncoder.encode(parameters).getTokenValue();
    }

    public Jwt validateToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            throw new BadJwtException("Invalid JWT token");
        }
    }
}
