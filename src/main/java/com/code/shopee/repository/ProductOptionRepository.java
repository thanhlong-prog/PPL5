package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.ProductOption;

public interface  ProductOptionRepository  extends JpaRepository<ProductOption, Integer> {
    ProductOption findByIdAndStatusTrue(int id);
    List<ProductOption> findByProductIdAndStatusTrue(int productId);
}
