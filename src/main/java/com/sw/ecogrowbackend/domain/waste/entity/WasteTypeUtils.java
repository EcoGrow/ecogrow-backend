package com.sw.ecogrowbackend.domain.waste.entity;

import java.util.Set;

public class WasteTypeUtils {

    private static final Set<String> RECYCLABLE_TYPES = Set.of(
        "plastic", "paper", "glass", "metal"
    );

    public static boolean isRecyclable(String wasteType) {
        return RECYCLABLE_TYPES.contains(wasteType.toLowerCase());
    }
}