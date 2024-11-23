package com.sw.ecogrowbackend.domain.chat.repository;

import com.sw.ecogrowbackend.domain.chat.entity.ChatRoom;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Set<ChatRoom> findAllByMembersContaining(User member);

    Optional<ChatRoom> findByMembersContainingAndMembersContaining(User member1, User member2);
}