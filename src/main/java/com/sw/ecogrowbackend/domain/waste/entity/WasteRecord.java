package com.sw.ecogrowbackend.domain.waste.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sw.ecogrowbackend.common.Timestamped;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "waste_record")
public class WasteRecord extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "wasteRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("wasteRecord")
    private List<WasteItem> wasteItems = new ArrayList<>(); // 여러 쓰레기 항목을 저장할 수 있도록 설정

    public WasteRecord(User user) {
        this.user = user;
    }

    public void addWasteItem(WasteItem item) {
        wasteItems.add(item);
        item.setWasteRecord(this);
    }
}