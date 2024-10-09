package com.sw.ecogrowbackend.domain.waste.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sw.ecogrowbackend.common.Timestamped;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "waste_record")
public class WasteRecord extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String wasteType; // 쓰레기 종류 (ex: 플라스틱, 음식물 쓰레기 등)

    @Column(nullable = false)
    private double amount; // 쓰레기 양 (예: kg 단위)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // 기본 생성자
    public WasteRecord(String wasteType, double amount, User user) {
        this.wasteType = wasteType;
        this.amount = amount;
        this.user = user;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}