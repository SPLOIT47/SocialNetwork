package com.usermicroservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.usermicroservice.entity.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
  RoleEntity findByName(String name);
}
