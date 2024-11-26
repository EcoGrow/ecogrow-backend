package com.sw.ecogrowbackend.domain.product.service;

import com.sw.ecogrowbackend.domain.product.dto.ProductResponseDto;
import com.sw.ecogrowbackend.domain.product.entity.Product;
import com.sw.ecogrowbackend.domain.product.repository.ProductRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final List<String> CATEGORY_URLS = List.of(
        "https://www.jigushop.co.kr/all",
        "https://www.jigushop.co.kr/dental",
        "https://www.jigushop.co.kr/bathroom",
        "https://www.jigushop.co.kr/kitchen",
        "https://www.jigushop.co.kr/food",
        "https://www.jigushop.co.kr/living",
        "https://www.jigushop.co.kr/gift"
    );

    private final ProductRepository productRepository;

    @Cacheable(key = "'crawl-by-category'", cacheNames = "products")
    public Page<ProductResponseDto> fetchProductsFromCategory(Pageable pageable, String category)
        throws IOException {

        List<Product> products = new ArrayList<>();
        Set<String> productNameSet = new HashSet<>();
        String targetUrl = CATEGORY_URLS.stream()
            .filter(url -> url.contains(category.toLowerCase()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid category"));

        Document document = Jsoup.connect(targetUrl)
            .userAgent(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
            .referrer("http://www.google.com")
            .timeout(5000)
            .get();

        Elements productElements = document.select("div.shop-item");

        for (Element element : productElements) {
            String name = element.select("h2.shop-title").text();
            if (name.isEmpty() || productNameSet.contains(name)) {
                continue;
            }
            productNameSet.add(name);

            String image = element.select("a.shop-item-thumb img._org_img").attr("abs:src");
            String url = element.select("a.shop-item-thumb").attr("abs:href");
            String priceText = element.select("p.pay").first().text();
            double price = 0.0;
            try {
                if (!priceText.isEmpty()) {
                    price = Double.parseDouble(priceText.replaceAll("[^0-9]", ""));
                }
            } catch (NumberFormatException e) {
                price = 0.0;
            }

            Product product = Product.builder()
                .name(name)
                .category(category)
                .price(price)
                .url(url.isEmpty() ? "URL 없음" : url)
                .image(image.isEmpty() ? "이미지 없음" : image)
                .build();

            products.add(product);
        }

        productRepository.saveAll(products);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), products.size());
        List<Product> pagedProducts = products.subList(start, end);

        return new PageImpl<>(pagedProducts, pageable, products.size())
            .map(ProductResponseDto::new);
    }
}