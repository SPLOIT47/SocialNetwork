package com.sploit.socialnetwork.auth.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import sploit.socialnetwork.shared.models.Role;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.role = :name")
    Optional<Role> findByName(@Param("name") String name);
}
