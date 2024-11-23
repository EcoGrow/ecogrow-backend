package com.sw.ecogrowbackend.domain.chat.service;

import com.sw.ecogrowbackend.domain.chat.MessageType;
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

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 유저 간의 1:1 채팅방에서 메시지 보내기
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

    // 두 유저 간의 메시지 기록 조회
    public List<ChatMessage> getChatMessages(Long userId1, Long userId2) {
        return chatMessageRepository.findMessagesBetweenUsers(userId1, userId2);
    }

    // 유저가 속해 있는 채팅방 조회
    public Set<ChatRoom> getUserChatRooms(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return chatRoomRepository.findAllByMembersContaining(user);
    }

    // 이메일을 통해 새로운 채팅방 생성
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
}
