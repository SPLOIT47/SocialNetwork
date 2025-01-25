package com.socialnetwork.messenger.entities.chat;

import com.socialnetwork.deps.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class GroupChat extends AbstractChat {
    private Collection<User> users;
}
