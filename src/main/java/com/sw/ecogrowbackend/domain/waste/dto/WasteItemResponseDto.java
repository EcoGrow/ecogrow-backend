package com.sw.ecogrowbackend.domain.waste.dto;

import com.sw.ecogrowbackend.domain.waste.entity.WasteItem;
import lombok.Getter;

@Getter
public class WasteItemResponseDto {

    private Long id;
    private String wasteType;
    private double amount;
    private String unit;
    private boolean isRecyclable;

    public WasteItemResponseDto(WasteItem wasteItem) {
        this.id = wasteItem.getId();
        this.wasteType = wasteItem.getWasteType();
        this.amount = wasteItem.getAmount();
        this.unit = wasteItem.getUnit();
        this.isRecyclable = wasteItem.isRecyclable();
    }
}
