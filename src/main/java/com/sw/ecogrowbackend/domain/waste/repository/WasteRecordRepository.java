package com.sw.ecogrowbackend.domain.waste.repository;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteRecordRepository extends JpaRepository<WasteRecord, Long> {

    List<WasteRecord> findAllByUser(User user);

    Page<WasteRecord> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 특정 사용자의 특정 id를 가진 단일 WasteRecord 조회.
    Optional<WasteRecord> findByIdAndUser(Long id, User user);

    // 특정 사용자의 모든 WasteRecord 조회.
    List<WasteRecord> findByUserId(Long userId);
}