package com.feedmicroservice.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.feedmicroservice.entity.CommentEntity;

public interface CommentRepository extends CrudRepository<CommentEntity, UUID>{
}
