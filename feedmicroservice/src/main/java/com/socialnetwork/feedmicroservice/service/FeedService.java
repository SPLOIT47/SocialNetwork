package com.socialnetwork.feedmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.socialnetwork.feedmicroservice.entity.SocialMediaEntity;
import com.socialnetwork.feedmicroservice.repository.CommentRepository;
import com.socialnetwork.feedmicroservice.repository.SocialMediaRepository;

@Service
public class FeedService {

    @Autowired
    SocialMediaRepository socialMediaRepository;

    @Autowired CommentRepository commentRepository;

    public HttpStatus publish(SocialMediaEntity entity) {
        return null;
    }
}
