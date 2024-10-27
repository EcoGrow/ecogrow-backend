package com.sw.ecogrowbackend.domain.waste.dto;

import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class WasteRecordResponseDto {

    private Long id;
    private List<WasteItemResponseDto> wasteItems;

    // WasteRecord를 인수로 받아 처리하는 생성자
    public WasteRecordResponseDto(WasteRecord wasteRecord) {
        this.id = wasteRecord.getId();
        this.wasteItems = wasteRecord.getWasteItems().stream()
            .map(WasteItemResponseDto::new)
            .collect(Collectors.toList());
    }

    // 엔티티를 Dto로 변환하는 메서드
    public static WasteRecordResponseDto fromEntity(WasteRecord wasteRecord) {
        return new WasteRecordResponseDto(wasteRecord);
    }
}