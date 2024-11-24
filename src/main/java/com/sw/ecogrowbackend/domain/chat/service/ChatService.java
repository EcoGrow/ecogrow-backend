package com.sw.ecogrowbackend.domain.chat.service;

import com.sw.ecogrowbackend.domain.chat.MessageType;
import com.sw.ecogrowbackend.domain.chat.dto.ChatMessageDto;
import com.sw.ecogrowbackend.domain.chat.entity.ChatMessage;
import com.sw.ecogrowbackend.domain.chat.entity.ChatRoom;
import com.sw.ecogrowbackend.domain.chat.repository.ChatMessageRepository;
import com.sw.ecogrowbackend.domain.chat.repository.ChatRoomRepository;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatMessage sendMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
            .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        ChatRoom chatRoom = chatRoomRepository.findByMembersContainingAndMembersContaining(sender, recipient)
            .orElseGet(() -> createChatRoom(sender, recipient));

        ChatMessage chatMessage = new ChatMessage(sender, recipient, content, MessageType.CHAT);
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageDto> getChatMessages(Long userId1, Long userId2) {
        return chatMessageRepository.findMessagesBetweenUsers(userId1, userId2).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    public Set<ChatRoom> getUserChatRooms(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return chatRoomRepository.findAllByMembersContaining(user);
    }

    public ChatRoom createChatRoomByEmail(Long userId, String recipientEmail) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User recipient = userRepository.findByEmail(recipientEmail)
            .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        return createChatRoom(user, recipient);
    }

    private ChatRoom createChatRoom(User user, User recipient) {
        ChatRoom newRoom = new ChatRoom(Set.of(user, recipient));
        return chatRoomRepository.save(newRoom);
    }

    private ChatMessageDto convertToDto(ChatMessage chatMessage) {
        return new ChatMessageDto(
            chatMessage.getSender().getId(),
            chatMessage.getSender().getUsername(),
            chatMessage.getContent(),
            chatMessage.getType(),
            chatMessage.getSender().getId(),
            chatMessage.getRecipient().getId(),
            chatMessage.getTimestamp()
        );
    }
}
