package com.sw.ecogrowbackend.domain.chat.controller;

import com.sw.ecogrowbackend.domain.chat.dto.ChatMessageDto;
import com.sw.ecogrowbackend.domain.chat.entity.ChatMessage;
import com.sw.ecogrowbackend.domain.chat.entity.ChatRoom;
import com.sw.ecogrowbackend.domain.chat.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody Map<String, Object> payload) {
        Long senderId = Long.valueOf(payload.get("senderId").toString());
        Long recipientId = Long.valueOf(payload.get("recipientId").toString());
        String content = payload.get("content").toString();
        return chatService.sendMessage(senderId, recipientId, content);
    }

    @GetMapping("/messages")
    public List<ChatMessageDto> getChatMessages(
        @RequestParam Long userId1,
        @RequestParam Long userId2
    ) {
        return chatService.getChatMessages(userId1, userId2);
    }

    @GetMapping("/rooms/{userId}")
    public Set<ChatRoom> getUserChatRooms(@PathVariable Long userId) {
        return chatService.getUserChatRooms(userId);
    }

    @PostMapping("/rooms/email")
    public ChatRoom createChatRoomByEmail(
        @RequestParam Long userId,
        @RequestParam String recipientEmail
    ) {
        return chatService.createChatRoomByEmail(userId, recipientEmail);
    }
}
