package com.feedmicroservice.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.feedmicroservice.entity.SocialMediaEntity;

public interface SocialMediaRepository extends CrudRepository<SocialMediaEntity, UUID>{
}
