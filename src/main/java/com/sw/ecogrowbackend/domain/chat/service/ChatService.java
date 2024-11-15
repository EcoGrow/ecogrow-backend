package com.sw.ecogrowbackend.domain.chat.service;

import com.sw.ecogrowbackend.domain.chat.entity.ChatMessage;
import com.sw.ecogrowbackend.domain.chat.repository.ChatMessageRepository;
import com.sw.ecogrowbackend.domain.profile.entity.Profile;
import com.sw.ecogrowbackend.domain.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ProfileRepository profileRepository;

    // 유저 간의 1:1 채팅방에서 메시지 보내기
    public ChatMessage sendMessage(Long senderId, Long recipientId, String content) {
        Profile sender = profileRepository.findById(senderId)
            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        Profile recipient = profileRepository.findById(recipientId)
            .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        ChatMessage chatMessage = new ChatMessage(sender, recipient, content,  com.sw.ecogrowbackend.domain.chat.MessageType.CHAT);

        return chatMessageRepository.save(chatMessage);
    }

    // 두 유저 간의 메시지 기록 조회
    public List<ChatMessage> getChatMessages(Long userId1, Long userId2) {
        return chatMessageRepository.findMessagesBetweenUsers(userId1, userId2);
    }
}
