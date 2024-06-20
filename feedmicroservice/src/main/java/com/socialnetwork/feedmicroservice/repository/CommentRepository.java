package com.socialnetwork.feedmicroservice.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.feedmicroservice.entity.CommentEntity;

public interface CommentRepository extends CrudRepository<CommentEntity, UUID>{
}
