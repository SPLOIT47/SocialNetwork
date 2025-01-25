package com.socialnetwork.messenger.entities;

import com.socialnetwork.messenger.entities.chat.AbstractChat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Message {
    @Id
    private UUID id;

    private UUID chatId;
    private String name;
    private UUID senderId;
    private String content;
    private MessageType type;
}
