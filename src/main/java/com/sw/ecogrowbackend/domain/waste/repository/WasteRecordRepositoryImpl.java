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
public class WasteRecordRepositoryImpl implements WasteRecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<WasteRecord> findFilteredWasteRecords(Long userId, String sortOption,
        String startDate, String endDate, Pageable pageable) {
        QWasteRecord wasteRecord = QWasteRecord.wasteRecord;

        // 기본 쿼리 생성
        var query = queryFactory.selectFrom(wasteRecord);

        // userId 조건 추가 (userId가 null이 아니면 필터링 적용)
        if (userId != null) {
            query.where(wasteRecord.user.id.eq(userId));
        }

        // 날짜 범위 필터링 (startDate와 endDate가 모두 있을 때만 적용)
        if (startDate != null && endDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            query.where(
                wasteRecord.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59)));
        }

        // 정렬 옵션 처리 (기본값을 최신순으로 설정)
        if ("oldest".equals(sortOption)) {
            query.orderBy(wasteRecord.createdAt.asc());
        } else {
            query.orderBy(wasteRecord.createdAt.desc()); // 기본값은 최신순
        }

        // 페이징된 결과 가져오기
        long total = query.fetchCount(); // 총 데이터 수 계산
        List<WasteRecord> results = query
            .offset(pageable.getOffset()) // 페이지 시작 위치
            .limit(pageable.getPageSize()) // 페이지 크기 제한
            .fetch(); // 결과 가져오기

        // 결과를 Page 객체로 변환하여 반환
        return new PageImpl<>(results, pageable, total);
    }
}