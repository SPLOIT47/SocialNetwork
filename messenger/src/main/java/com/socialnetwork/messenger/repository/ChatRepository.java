package com.socialnetwork.messenger.repository;

import com.socialnetwork.messenger.entities.chat.AbstractChat;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ChatRepository extends CrudRepository<AbstractChat, UUID> {
}
