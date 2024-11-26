package com.sw.ecogrowbackend.domain.product.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private double price;
    private String url;
    private String image;

//    @ManyToMany(mappedBy = "products")
//    private List<RecommendationCategory> recommendationCategories = new ArrayList<>();
//
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<UserRecommendation> userRecommendations = new ArrayList<>();

    @Builder
    public Product(String name, String category, double price, String url,
        String image) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.url = url;
        this.image = image;
    }
}