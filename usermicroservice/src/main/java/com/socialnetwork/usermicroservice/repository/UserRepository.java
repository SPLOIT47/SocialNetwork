package com.socialnetwork.usermicroservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.usermicroservice.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, String> {
    Optional<UserEntity> findByNickname(String username);
}
