package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.ProductOptions;



public interface ProductOptionsReposioty extends JpaRepository<ProductOptions, Integer> {
    ProductOptions findByProductIdAndStatusTrue(int productId);
    List<ProductOptions> findByProductIdAndStatusTrueOrderByIdAsc(int productId);
}
