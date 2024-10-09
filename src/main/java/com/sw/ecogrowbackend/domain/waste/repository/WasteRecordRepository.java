package com.sw.ecogrowbackend.domain.waste.repository;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteRecordRepository extends JpaRepository<WasteRecord, Long> {

    List<WasteRecord> findAllByUser(User user);

    Optional<WasteRecord> findByIdAndUser(Long id, User user);

}