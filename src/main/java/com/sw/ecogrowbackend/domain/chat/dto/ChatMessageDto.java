package com.sw.ecogrowbackend.domain.chat.dto;

import com.sw.ecogrowbackend.domain.chat.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ChatMessageDto {

    @Setter
    private Long userId;
    @Setter
    private String sender;
    @Setter
    private String content;
    // 새롭게 추가된 setType 메서드
    @Setter
    private MessageType type;  // MessageType 필드
    private Long senderId;
    private Long recipientId;

    // 기본 생성자
    public ChatMessageDto() {
    }

    // 모든 필드를 초기화하는 생성자
    public ChatMessageDto(Long userId, String sender, String content, MessageType type) {
        this.userId = userId;
        this.sender = sender;
        this.content = content;
        this.type = type;
    }
}