package com.sw.ecogrowbackend.domain.chat.controller;

import com.sw.ecogrowbackend.domain.chat.entity.ChatMessage;
import com.sw.ecogrowbackend.domain.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 특정 유저와 채팅 시작
    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestParam Long senderId,
        @RequestParam Long recipientId,
        @RequestParam String content) {
        return chatService.sendMessage(senderId, recipientId, content);
    }

    // 두 유저 간의 채팅 기록 조회
    @GetMapping("/messages")
    public List<ChatMessage> getChatMessages(@RequestParam Long userId1,
        @RequestParam Long userId2) {
        return chatService.getChatMessages(userId1, userId2);
    }
}