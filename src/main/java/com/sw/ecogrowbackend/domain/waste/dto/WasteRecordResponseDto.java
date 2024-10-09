package com.sw.ecogrowbackend.domain.waste.dto;

import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WasteRecordResponseDto {

    private Long id;
    private String wasteType;
    private double amount;

    public WasteRecordResponseDto(Long id, String wasteType, double amount
    ) {
        this.id = id;
        this.wasteType = wasteType;
        this.amount = amount;
    }

    // 엔티티를 Dto로 변환하는 메서드
    public static WasteRecordResponseDto fromEntity(WasteRecord wasteRecord) {
        return WasteRecordResponseDto.builder()
            .id(wasteRecord.getId())
            .wasteType(wasteRecord.getWasteType())
            .amount(wasteRecord.getAmount())
            .build();
    }
}