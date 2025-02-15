package com.sploit.socialnetwork.auth.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import sploit.socialnetwork.shared.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
