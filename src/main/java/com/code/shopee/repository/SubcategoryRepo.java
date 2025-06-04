package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.Subcategory;

public interface SubcategoryRepo extends JpaRepository<Subcategory, Integer> {
    List<Subcategory> findByCategoryIdAndStatusTrue(int id);
    Subcategory findByIdAndStatusTrue(int id);
}
