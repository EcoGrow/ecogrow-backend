package com.sw.ecogrowbackend.domain.chat.controller;

import com.sw.ecogrowbackend.domain.chat.entity.ChatMessage;
import com.sw.ecogrowbackend.domain.chat.entity.ChatRoom;
import com.sw.ecogrowbackend.domain.chat.service.ChatService;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // 프론트엔드 도메인 주소 허용
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 특정 유저와 채팅 시작
    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody Map<String, Object> payload) {
        Long senderId = Long.valueOf(payload.get("senderId").toString());
        Long recipientId = Long.valueOf(payload.get("recipientId").toString());
        String content = payload.get("content").toString();
        return chatService.sendMessage(senderId, recipientId, content);
    }

    // 두 유저 간의 채팅 기록 조회
    @GetMapping("/messages")
    public List<ChatMessage> getChatMessages(@RequestParam Long userId1,
        @RequestParam Long userId2) {
        return chatService.getChatMessages(userId1, userId2);
    }

    // 유저가 속해 있는 채팅방 조회
    @GetMapping("/rooms/{userId}")
    public Set<ChatRoom> getUserChatRooms(@PathVariable Long userId) {
        return chatService.getUserChatRooms(userId);
    }

    // 이메일로 유저 검색 후 새로운 채팅방 생성
    @PostMapping("/rooms/email")
    public ChatRoom createChatRoomByEmail(@RequestParam Long userId, @RequestParam String recipientEmail) {
        return chatService.createChatRoomByEmail(userId, recipientEmail);
    }
}
