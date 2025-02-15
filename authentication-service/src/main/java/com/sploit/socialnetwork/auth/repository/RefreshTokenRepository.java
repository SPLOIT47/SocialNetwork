package com.sploit.socialnetwork.auth.repository;

import com.sploit.socialnetwork.auth.models.RefreshToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository  {
    void add(RefreshToken refreshToken);
    long delete(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    long deleteByUserId(UUID Id);
    public List<RefreshToken> findByUserId(UUID Id);
}
