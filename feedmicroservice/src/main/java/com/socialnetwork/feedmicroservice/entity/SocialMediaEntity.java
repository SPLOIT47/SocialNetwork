package com.socialnetwork.feedmicroservice.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class SocialMediaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  UUID id;

  @Column(unique = false, nullable = false)
  String mediaName;

  @OneToMany(mappedBy = "socialMediaEntity")
  List<CommentEntity> comment = new ArrayList<>();
  
  String publisher;
  
  String mediaContent;

  Long likes;

  Long commentsCount;
  
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getMediaName() {
    return mediaName;
  }

  public void setMediaName(String mediaName) {
    this.mediaName = mediaName;
  }

  public List<CommentEntity> getComment() {
    return comment;
  }

  public void setComment(List<CommentEntity> comment) {
    this.comment = comment;
  }

  public String getMediaContent() {
    return mediaContent;
  }

  public void setMediaContent(String mediaContent) {
    this.mediaContent = mediaContent;
  }

  public Long getLikes() {
    return likes;
  }

  public void setLikes(Long likes) {
    this.likes = likes;
  }

  public Long getCommentsCount() {
    return commentsCount;
  }

  public void setCommentsCount(Long commentsCount) {
    this.commentsCount = commentsCount;
  }

  public SocialMediaEntity(Long likes) {
    this.likes = likes;
  }
}
