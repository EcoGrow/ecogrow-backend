package com.sw.ecogrowbackend.domain.waste.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sw.ecogrowbackend.domain.waste.entity.QWasteRecord;
import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WasteRecordRepositoryImpl implements
    WasteRecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<WasteRecord> findFilteredWasteRecords(Long userId, String sortOption,
        String startDate, String endDate, Pageable pageable) {
        QWasteRecord wasteRecord = QWasteRecord.wasteRecord;

        var query = queryFactory.selectFrom(wasteRecord)
            .where(wasteRecord.user.id.eq(userId));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            query.where(
                wasteRecord.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59)));
        }

        switch (sortOption) {
            case "newest":
                query.orderBy(wasteRecord.createdAt.desc());
                break;
            case "oldest":
                query.orderBy(wasteRecord.createdAt.asc());
                break;
        }

        long total = query.fetchCount();
        List<WasteRecord> results = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(results, pageable, total);
    }
}