package com.socialnetwork.feedmicroservice.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.feedmicroservice.entity.SocialMediaEntity;

public interface SocialMediaRepository extends CrudRepository<SocialMediaEntity, UUID>{
}
