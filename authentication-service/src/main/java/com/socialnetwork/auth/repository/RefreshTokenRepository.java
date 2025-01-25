package com.socialnetwork.auth.repository;

import com.socialnetwork.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID> {
}
