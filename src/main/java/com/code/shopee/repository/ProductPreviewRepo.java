package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.ProductPreview;

public interface ProductPreviewRepo extends JpaRepository<ProductPreview, Integer> {
    ProductPreview findByProductIdAndStatusTrue(int productId);
    ProductPreview findByProductIdAndStatusFalse(int productId);
    ProductPreview findByIdAndStatusTrue(int id);
    ProductPreview findByIdAndStatusFalse(int id);
    List<ProductPreview> findAllByProductIdAndStatusTrue(int productId);
}
