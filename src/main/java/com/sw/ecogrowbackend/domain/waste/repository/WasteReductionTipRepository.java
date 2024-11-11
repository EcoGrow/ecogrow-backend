package com.sw.ecogrowbackend.domain.waste.repository;

import com.sw.ecogrowbackend.domain.waste.entity.WasteReductionTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteReductionTipRepository extends JpaRepository<WasteReductionTip, Long> {
}