package com.sw.ecogrowbackend.domain.product.controller;

import com.sw.ecogrowbackend.domain.product.dto.ProductResponseDto;
import com.sw.ecogrowbackend.domain.product.service.ProductService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 크롤링을 통해 제로웨이스트 제품 정보 가져오기
     *
     * @return 크롤링된 제품 목록 (DTO 형태)
     */
    @GetMapping("/crawl")
    public Page<ProductResponseDto> fetchProductsFromCrawling(Pageable pageable, String category)
        throws IOException {
        return productService.fetchProductsFromCategory(pageable, category);
    }
}