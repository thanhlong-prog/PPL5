package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.ProductOptionValues;

public interface ProductOptionValuesRepo extends JpaRepository<ProductOptionValues, Integer> {
    ProductOptionValues findByOptionIdAndStatusTrue(int optionId);
    List<ProductOptionValues> findByOptionIdAndStatusTrueOrderByIdAsc(int optionId);
    
}
