package com.socialnetwork.usermicroservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.usermicroservice.entity.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}
