package com.sw.ecogrowbackend.domain.waste.service;

import com.sw.ecogrowbackend.common.WasteReductionTipMessage;
import com.sw.ecogrowbackend.domain.waste.dto.WasteReductionTipResponseDto;
import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import com.sw.ecogrowbackend.domain.waste.entity.WasteItem;
import com.sw.ecogrowbackend.domain.waste.repository.WasteRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WasteReductionTipService {

    private final WasteRecordRepository wasteRecordRepository;

    private static final double PLASTIC_THRESHOLD = 8.0;
    private static final double GLASS_THRESHOLD = 3.0;
    private static final double GENERAL_WASTE_THRESHOLD = 15.0;
    private static final double METAL_THRESHOLD = 2.5;
    private static final double ORGANIC_THRESHOLD = 8.0;
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

        // 3. 기간별 배출량 증가 추세 분석
        checkEmissionTrend(records, tips);

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

        double organicAmount = getTotalWasteAmount(records, "organic");
        if (organicAmount > ORGANIC_THRESHOLD) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.ORGANIC_REDUCTION_TIP.getMsg()));
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

    private void checkEmissionTrend(List<WasteRecord> records,
        List<WasteReductionTipResponseDto> tips) {
        List<Double> monthlyEmissions = getMonthlyEmissions(records); // 월별 배출량을 계산한다고 가정

        if (monthlyEmissions.isEmpty() || records.isEmpty()) {
            return;
        }

        boolean isIncreasingTrend = true;
        for (int i = 1; i < monthlyEmissions.size(); i++) {
            if (monthlyEmissions.get(i) <= monthlyEmissions.get(i - 1)) {
                isIncreasingTrend = false;
                break;
            }
        }

        // 배출량이 증가 추세일 경우 팁을 추가
        if (isIncreasingTrend) {
            tips.add(new WasteReductionTipResponseDto(
                WasteReductionTipMessage.INCREASING_EMISSION_TREND_TIP.getMsg()));
        }
    }

    // 월별 배출량을 계산하는 메서드
    private List<Double> getMonthlyEmissions(List<WasteRecord> records) {
        return new ArrayList<>();
    }
}