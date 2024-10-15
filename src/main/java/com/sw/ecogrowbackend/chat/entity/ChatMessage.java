package com.sw.ecogrowbackend.chat.entity;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.chat.MessageType;
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
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    public ChatMessage(User user, String content, MessageType type) {
        this.user = user;
        this.content = content;
        this.type = type;
    }
}