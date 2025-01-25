package com.socialnetwork.messenger.repository;

import com.socialnetwork.messenger.entities.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message, UUID> {
}
