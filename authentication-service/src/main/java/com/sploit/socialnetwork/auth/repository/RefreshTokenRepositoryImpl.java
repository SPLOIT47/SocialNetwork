package com.sploit.socialnetwork.auth.repository;

import com.sploit.socialnetwork.auth.models.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private static final String HASH_KEY = "RefreshToken";
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RefreshTokenRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void add(RefreshToken refreshToken) {
        redisTemplate.opsForHash().put(HASH_KEY, refreshToken.getId(), refreshToken);

        String userIdKey = getUserIndexKey(refreshToken.getUserId());
        redisTemplate.opsForSet().add(userIdKey, refreshToken.getId());
    }

    @Override
    public long delete(RefreshToken refreshToken) {
        long removed = redisTemplate.opsForHash().delete(HASH_KEY, refreshToken.getId());

        String userIdKey = getUserIndexKey(refreshToken.getUserId());
        redisTemplate.opsForSet().remove(HASH_KEY, userIdKey);

        return removed;
    }

    @Override
    public long deleteByUserId(UUID Id) {
        String index = getUserIndexKey(Id);

        Set<Object> tokens = redisTemplate.opsForSet().members(index);

        if (tokens == null || tokens.isEmpty()) {
            return 0;
        }

        long removed = 0;
        for (Object token : tokens) {
            removed += redisTemplate.opsForHash().delete(HASH_KEY, token.toString());
            removed++;
        }

        redisTemplate.delete(index);

        return removed;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        RefreshToken instance = (RefreshToken) redisTemplate.opsForHash().get(HASH_KEY, token);
        return instance == null ? Optional.empty() : Optional.of(instance);
    }

    @Override
    public List<RefreshToken> findByUserId(UUID Id) {
        String index = getUserIndexKey(Id);
        Set<Object> objects =  redisTemplate.opsForSet().members(index);
        if (objects == null || objects.isEmpty()) {
            return new ArrayList<>();
        }

        return objects.stream().map(o -> (RefreshToken) o).collect(Collectors.toList());
    }

    private String getUserIndexKey(UUID Id) {
        return "UserIndex:" + Id;
    }
}

