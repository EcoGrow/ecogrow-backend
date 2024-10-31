package com.sw.ecogrowbackend.domain.waste.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WasteReductionTipReponseDto {

    private Long id;

    private String wasteType; // 팁의 대상 쓰레기 종류

    private String tipMessage; // 팁 메시지

    private boolean isRecyclable; // 재활용 가능 여부 기반 팁 여부
}