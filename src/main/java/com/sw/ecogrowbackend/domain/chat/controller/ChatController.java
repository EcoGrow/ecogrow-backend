package com.sw.ecogrowbackend.domain.chat.controller;

import com.sw.ecogrowbackend.domain.chat.dto.ChatMessageDto;
import com.sw.ecogrowbackend.domain.chat.MessageType;
import com.sw.ecogrowbackend.domain.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(ChatMessageDto chatMessage) {
        return chatService.processMessage(chatMessage);
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/public")
    public ChatMessageDto newUser(ChatMessageDto chatMessage) {
        chatMessage.setType(MessageType.JOIN);  // 이제 이 부분에서 오류가 발생하지 않음
        return chatService.processMessage(chatMessage);
    }
}