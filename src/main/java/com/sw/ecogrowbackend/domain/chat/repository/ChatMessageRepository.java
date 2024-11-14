package com.sw.ecogrowbackend.domain.chat.repository;

import com.sw.ecogrowbackend.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 필요한 쿼리 메서드 정의 가능
}