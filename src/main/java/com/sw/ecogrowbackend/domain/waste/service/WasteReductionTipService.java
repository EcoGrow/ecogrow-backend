package com.sw.ecogrowbackend.domain.waste.service;

import com.sw.ecogrowbackend.common.WasteReductionTipMessage;
import com.sw.ecogrowbackend.domain.waste.dto.WasteReductionTipResponseDto;
import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import com.sw.ecogrowbackend.domain.waste.entity.WasteItem;
import com.sw.ecogrowbackend.domain.waste.entity.WasteReductionTip;
import com.sw.ecogrowbackend.domain.waste.repository.WasteRecordRepository;
import com.sw.ecogrowbackend.domain.waste.repository.WasteReductionTipRepository;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WasteReductionTipService {

    private final WasteRecordRepository wasteRecordRepository;
    private final WasteReductionTipRepository wasteReductionTipRepository;

    private static final double PLASTIC_THRESHOLD = 8.0;
    private static final double GLASS_THRESHOLD = 3.0;
    private static final double GENERAL_WASTE_THRESHOLD = 15.0;
    private static final double METAL_THRESHOLD = 2.5;
    private static final double FOOD_THRESHOLD = 8.0;
    private static final double PAPER_THRESHOLD = 7.0;
    private static final double RECYCLABLE_PERCENTAGE_THRESHOLD = 30.0;

    /**
     * 특정 사용자의 쓰레기 기록을 기반으로 감소 팁을 생성
     *
     * @param userId 사용자 ID
     * @return 사용자의 쓰레기 배출 습관에 따른 감소 팁 목록
     */
    public List<WasteReductionTipResponseDto> getTipsForUser(Long userId) {
        List<WasteRecord> records = wasteRecordRepository.findByUserId(userId);
        List<WasteReductionTipResponseDto> tips = new ArrayList<>();

        // 1. 각 쓰레기 종류에 대한 높은 배출량 감지
        checkHighEmissions(records, tips);

        // 2. 재활용 가능 품목의 비율 확인
        checkRecyclableProportion(records, tips);

        // 3. 최근 기간별 배출량 증가 추세 분석
        checkRecentEmissionTrend(records, tips);

        // 4. 월별 배출량 분석
        checkMonthlyEmissionTrend(records, tips);

        return tips;
    }

    private void checkHighEmissions(List<WasteRecord> records,
        List<WasteReductionTipResponseDto> tips) {
        double plasticAmount = getTotalWasteAmount(records, "plastic");
        if (plasticAmount > PLASTIC_THRESHOLD) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.PLASTIC_REDUCTION_TIP.getMsg()));
        }

        double glassAmount = getTotalWasteAmount(records, "glass");
        if (glassAmount > GLASS_THRESHOLD) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.GLASS_REDUCTION_TIP.getMsg()));
        }

        double generalWasteAmount = getTotalWasteAmount(records, "general");
        if (generalWasteAmount > GENERAL_WASTE_THRESHOLD) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.GENERAL_WASTE_REDUCTION_TIP.getMsg()));
        }

        double metalAmount = getTotalWasteAmount(records, "metal");
        if (metalAmount > METAL_THRESHOLD) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.METAL_REDUCTION_TIP.getMsg()));
        }

        double foodAmount = getTotalWasteAmount(records, "food");
        if (foodAmount > FOOD_THRESHOLD) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.FOOD_REDUCTION_TIP.getMsg()));
        }

        double paperAmount = getTotalWasteAmount(records, "paper");
        if (paperAmount > PAPER_THRESHOLD) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.PAPER_REDUCTION_TIP.getMsg()));
        }
    }

    private double getTotalWasteAmount(List<WasteRecord> records, String wasteType) {
        return records.stream()
            .flatMap(record -> record.getWasteItems().stream())
            .filter(item -> wasteType.equalsIgnoreCase(item.getWasteType()))
            .mapToDouble(WasteItem::getAmount)
            .sum();
    }

    private void checkRecyclableProportion(List<WasteRecord> records,
        List<WasteReductionTipResponseDto> tips) {
        long totalItems = records.stream()
            .mapToLong(record -> record.getWasteItems().size())
            .sum();

        long recyclableItems = records.stream()
            .flatMap(record -> record.getWasteItems().stream())
            .filter(WasteItem::isRecyclable)
            .count();

        double recyclablePercentage = (double) recyclableItems / totalItems * 100;

        if (recyclablePercentage < RECYCLABLE_PERCENTAGE_THRESHOLD) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.RECYCLABLE_PROPORTION_TIP.getMsg()));
        }
    }

    // 최근 배출량 증가 추세 확인 메서드
    private void checkRecentEmissionTrend(List<WasteRecord> records,
        List<WasteReductionTipResponseDto> tips) {

        // 최신 기록을 날짜 순으로 정렬 후 최근 3개의 배출량만 가져옴
        List<Double> recentEmissions = records.stream()
            .sorted(Comparator.comparing(WasteRecord::getCreatedAt).reversed())
            .limit(3) // 최근 3개의 기록만 사용
            .map(record -> record.getWasteItems().stream()
                .mapToDouble(WasteItem::getAmount)
                .sum())
            .collect(Collectors.toList());

        // 최근 배출량이 증가 추세인지 확인
        boolean isIncreasingTrend = true;
        for (int i = 1; i < recentEmissions.size(); i++) {
            if (recentEmissions.get(i) <= recentEmissions.get(i - 1)) {
                isIncreasingTrend = false;
                break;
            }
        }

        if (isIncreasingTrend && recentEmissions.size() == 3) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.INCREASING_EMISSION_TREND_TIP.getMsg()));
        }
    }

    // 월별 배출량 확인 메서드
    private void checkMonthlyEmissionTrend(List<WasteRecord> records,
        List<WasteReductionTipResponseDto> tips) {

        List<Double> monthlyEmissions = getMonthlyEmissions(records); // 월별 배출량 계산
        if (monthlyEmissions.size() < 2) {
            return;
        }

        boolean isIncreasingTrend = true;
        for (int i = 1; i < monthlyEmissions.size(); i++) {
            if (monthlyEmissions.get(i) <= monthlyEmissions.get(i - 1)) {
                isIncreasingTrend = false;
                break;
            }
        }

        if (isIncreasingTrend) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.MONTHLY_INCREASING_EMISSION_TREND_TIP.getMsg()));
        }
    }

    // 월별 배출량을 계산하는 메서드
    private List<Double> getMonthlyEmissions(List<WasteRecord> records) {

        if (records.isEmpty()) {
            return new ArrayList<>();
        }

        // WasteRecord의 날짜를 기준으로 YearMonth로 그룹화하여 월별 배출량을 합산
        Map<YearMonth, Double> monthlyEmissionsMap = records.stream()
            .collect(Collectors.groupingBy(
                record -> YearMonth.from(record.getCreatedAt()), // YearMonth 단위로 그룹화
                Collectors.summingDouble(record -> record.getWasteItems().stream()
                    .mapToDouble(WasteItem::getAmount)
                    .sum())
            ));

        // YearMonth 기준으로 정렬하고, 월별 배출량을 리스트로 변환
        List<Double> monthlyEmissions = monthlyEmissionsMap.entrySet().stream()
            .sorted(Map.Entry.comparingByKey()) // YearMonth 기준으로 정렬
            .map(Map.Entry::getValue) // 배출량 값만 추출
            .collect(Collectors.toList());
        return monthlyEmissions;
    }

    // 3일 지난 팁을 삭제하는 스케줄러
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void deleteOldTips() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(3);
        List<WasteReductionTip> oldTips = wasteReductionTipRepository.findByCreatedAtBefore(
            cutoffDate);
        wasteReductionTipRepository.deleteAll(oldTips);
    }
}