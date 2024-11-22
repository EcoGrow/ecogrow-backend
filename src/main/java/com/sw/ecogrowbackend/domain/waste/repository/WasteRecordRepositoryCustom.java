package com.sw.ecogrowbackend.domain.waste.repository;

import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WasteRecordRepositoryCustom {
    Page<WasteRecord> findFilteredWasteRecords(Long userId, String sortOption, String startDate, String endDate, Pageable pageable);
}
