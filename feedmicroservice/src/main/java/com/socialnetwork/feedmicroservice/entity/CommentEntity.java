package com.socialnetwork.feedmicroservice.entity;

import java.util.UUID;

// import com.usermicroservice.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class CommentEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  UUID id;

  @ManyToOne
  SocialMediaEntity socialMediaEntity;

  String publisher;

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  String content;

  public CommentEntity(String content) {
    this.content = content;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public SocialMediaEntity getSocialMediaEntity() {
    return socialMediaEntity;
  }

  public void setSocialMediaEntity(SocialMediaEntity socialMediaEntity) {
    this.socialMediaEntity = socialMediaEntity;
  }
}
