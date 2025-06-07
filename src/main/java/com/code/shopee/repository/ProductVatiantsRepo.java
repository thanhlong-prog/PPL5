package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.ProductVatiants;
import com.code.shopee.model.User;

public interface ProductVatiantsRepo extends JpaRepository<ProductVatiants, Integer> {
    ProductVatiants findByProductIdAndStatusTrue(int productId);
    List<ProductVatiants> findByProductIdAndStatusTrueOrderByIdAsc(int productId);
    List<ProductVatiants> findByCreatedBy(User createdBy);
}
