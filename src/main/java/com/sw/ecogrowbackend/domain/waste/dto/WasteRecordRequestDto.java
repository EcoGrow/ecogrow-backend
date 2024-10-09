package com.sw.ecogrowbackend.domain.waste.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class WasteRecordRequestDto {

    @NotBlank(message = "쓰레기 종류는 비워둘 수 없습니다.")
    private String wasteType;

    @Min(value = 0, message = "양은 0보다 커야 합니다.")
    private double amount;
}