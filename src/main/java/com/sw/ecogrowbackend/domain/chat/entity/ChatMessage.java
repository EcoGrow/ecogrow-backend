package com.sw.ecogrowbackend.domain.chat.entity;

import com.sw.ecogrowbackend.domain.chat.MessageType;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    private String content;

    private Long timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    public ChatMessage(User sender, User recipient, String content, MessageType type) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }
}