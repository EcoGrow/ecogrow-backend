package com.sw.ecogrowbackend.domain.chat.repository;

import com.sw.ecogrowbackend.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE (m.sender.id = :userId1 AND m.recipient.id = :userId2) " +
        "OR (m.sender.id = :userId2 AND m.recipient.id = :userId1) ORDER BY m.timestamp ASC")
    List<ChatMessage> findMessagesBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}