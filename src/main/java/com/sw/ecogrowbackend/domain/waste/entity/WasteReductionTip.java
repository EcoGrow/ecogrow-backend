package com.sw.ecogrowbackend.domain.waste.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class WasteReductionTip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String wasteType; // 팁의 대상 쓰레기 종류

    private String tipMessage; // 팁 메시지

    private boolean isRecyclable; // 재활용 가능 여부 기반 팁 여부

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waste_record_id", nullable = false)
    private WasteRecord wasteRecord;
}