package com.socialnetwork.messenger.entities.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class DirectChat extends AbstractChat {
    private UUID firstUserId;
    private UUID secondUserId;
}
