package com.sw.ecogrowbackend.domain.chat.dto;

import com.sw.ecogrowbackend.domain.chat.MessageType;

public class ChatMessageDto {
    private final Long userId;
    private final String sender;
    private final String content;
    private final MessageType type;
    private final Long senderId;
    private final Long recipientId;

    public ChatMessageDto(Long userId, String sender, String content, MessageType type, Long senderId, Long recipientId) {
        this.userId = userId;
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public MessageType getType() {
        return type;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }
}
