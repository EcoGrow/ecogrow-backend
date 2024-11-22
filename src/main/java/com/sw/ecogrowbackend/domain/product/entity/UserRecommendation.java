package com.sw.ecogrowbackend.domain.product.entity;

import com.sw.ecogrowbackend.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 추천 대상

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; //추천되는 것

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private RecommendationCategory recommendationCategory; // 제품이 권장되는 이유를 분류("플라스틱 줄이기")

    private boolean isActive;
}