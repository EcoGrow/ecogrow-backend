package com.sw.ecogrowbackend.domain.waste.repository;

import com.sw.ecogrowbackend.domain.waste.entity.WasteReductionTip;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteReductionTipRepository extends JpaRepository<WasteReductionTip, Long> {

    // 생성된 지 3일 이상 지난 팁 조회
    List<WasteReductionTip> findByCreatedAtBefore(LocalDateTime cutoffDate);
}