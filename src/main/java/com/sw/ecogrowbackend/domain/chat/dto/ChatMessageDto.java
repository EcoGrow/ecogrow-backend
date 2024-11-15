package com.sw.ecogrowbackend.domain.chat.dto;

import com.sw.ecogrowbackend.domain.chat.MessageType;

public class ChatMessageDto {

    private Long userId;
    private String sender;
    private String content;
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

    // Getter & Setter 메서드들

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    // 새롭게 추가된 setType 메서드
    public void setType(MessageType type) {
        this.type = type;
    }
}