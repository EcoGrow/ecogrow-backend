package com.sw.ecogrowbackend.domain.product.dto;

import com.sw.ecogrowbackend.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ProductResponseDto {

    private String name;       // 제품 이름
    private String image;      // 썸네일 또는 제품 이미지
    private String url;        // 제품 세부 URL
    private String category;   // 제품 카테고리
    private double price;

    public ProductResponseDto(String name, String image, String url, String category,
        double price) {
        this.name = name;
        this.image = image;
        this.url = url;
        this.category = category;
        this.price = price;
    }

    // Product 엔티티에서 DTO로 변환하는 생성자 추가
    public ProductResponseDto(Product product) {
        this.name = product.getName();
        this.image = product.getImage();
        this.url = product.getUrl();
        this.category = product.getCategory();
        this.price = product.getPrice();
    }
}