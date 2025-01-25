package com.socialnetwork.messenger.controller;

import com.socialnetwork.messenger.entities.Message;
import com.socialnetwork.messenger.entities.chat.DirectChat;
import com.socialnetwork.messenger.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class ChatController {

    @Autowired
    private final ChatRepository chatRepository;

    public ChatController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/{chatId}")
    public Message sendMessage(
            @Payload Message chatMessage,
            @DestinationVariable UUID chatId
    ) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/{chatId}")
    public Message addUser(
            @Payload Message chatMessage,
            @DestinationVariable UUID chatId,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        if (sessionAttributes == null) {
            sessionAttributes = new HashMap<>();
            headerAccessor.setSessionAttributes(sessionAttributes);
        }

        sessionAttributes.put("username", chatMessage.getName());
        sessionAttributes.put("chatId", chatId);

        return chatMessage;
    }

    @PostMapping("/app/chat.create")
    public ResponseEntity<String> createChat(@RequestParam UUID firstUserId, @RequestParam UUID secondUserId) {
        UUID chatId;
        boolean exists;
        do {
            chatId = UUID.randomUUID();
            exists = chatRepository.existsById(chatId);
        } while (exists);

        DirectChat chat = new DirectChat();
        chat.setId(chatId);
        chat.setFirstUserId(firstUserId);
        chat.setSecondUserId(secondUserId);
        chatRepository.save(chat);

        return ResponseEntity.ok(chatId.toString());
    }

}
