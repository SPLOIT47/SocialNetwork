package com.sploit.socialnetwork.auth.security.services;

import com.sploit.socialnetwork.auth.exception.TokenRefreshException;
import com.sploit.socialnetwork.auth.models.RefreshToken;
import com.sploit.socialnetwork.auth.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("jwt.refresh-expiration-millis")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
         return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(UUID id) {
        return RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .userId(id)
                .build();
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException(refreshToken.getRefreshToken(),
                    "Refresh token was expired. Please make a new signin request");
        }
        return refreshToken;
    }

    @Transactional
    public long deleteByUserId(UUID userId) {
        return refreshTokenRepository.deleteByUserId(userId);
    }
}
