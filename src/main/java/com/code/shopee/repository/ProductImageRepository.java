package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    ProductImage findByIdAndStatusTrue(int id);
    List<ProductImage> findByProductIdAndStatusTrue(int productId);
}
