package com.sw.ecogrowbackend.domain.chat.service;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import com.sw.ecogrowbackend.domain.chat.dto.ChatMessageDto;
import com.sw.ecogrowbackend.domain.chat.entity.ChatMessage;
import com.sw.ecogrowbackend.domain.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatService(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    public ChatMessageDto processMessage(ChatMessageDto messageDto) {
        User user = userRepository.findById(messageDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ChatMessage message = new ChatMessage(user, messageDto.getContent(), messageDto.getType());
        chatMessageRepository.save(message);

        return messageDto;
    }
}