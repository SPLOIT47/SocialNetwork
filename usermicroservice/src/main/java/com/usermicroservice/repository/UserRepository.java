package com.usermicroservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.usermicroservice.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, UUID>{
  Optional<UserEntity> findByUsername(String username); 
}
