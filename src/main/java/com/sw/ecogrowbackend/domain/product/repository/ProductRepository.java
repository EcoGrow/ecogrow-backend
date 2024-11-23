package com.sw.ecogrowbackend.domain.product.repository;

import com.sw.ecogrowbackend.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}