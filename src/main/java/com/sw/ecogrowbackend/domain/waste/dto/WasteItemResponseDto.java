package com.sw.ecogrowbackend.domain.waste.dto;

import com.sw.ecogrowbackend.domain.waste.entity.WasteItem;
import lombok.Getter;

@Getter
public class WasteItemResponseDto {

    private String wasteType;
    private double amount;
    private String unit;

    public WasteItemResponseDto(WasteItem wasteItem) {
        this.wasteType = wasteItem.getWasteType();
        this.amount = wasteItem.getAmount();
        this.unit = wasteItem.getUnit();
    }
}
