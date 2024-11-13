package com.sw.ecogrowbackend.domain.waste.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WasteReductionTipResponseDto {

    private Long id;

    private String wasteType; // 팁의 대상 쓰레기 종류

    private String tips; // 팁 메시지

    private boolean isRecyclable; // 재활용 가능 여부 기반 팁 여부

    public WasteReductionTipResponseDto(String tips) {
        this.id = null;
        this.wasteType = null;
        this.tips = tips;
        this.isRecyclable = false;
    }
}