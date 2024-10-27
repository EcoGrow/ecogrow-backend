package com.sw.ecogrowbackend.domain.waste.dto;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WasteRecordRequestDto {

    @Valid
    private List<WasteItemRequestDto> wasteItems;
}