package com.sw.ecogrowbackend.domain.waste.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class WasteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String wasteType; // 쓰레기 종류 (ex: 플라스틱, 음식물 쓰레기 등)

    @Setter
    @Column(nullable = false)
    private double amount; // 쓰레기 양 (예: kg 단위)

    @Setter
    @Column(nullable = false)
    private String unit; // 단위

    @Setter
    private boolean isRecyclable; // 재활용 가능 여부

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waste_record_id")
    private WasteRecord wasteRecord;
}