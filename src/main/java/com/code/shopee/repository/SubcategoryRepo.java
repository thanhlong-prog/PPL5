package com.code.shopee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.Subcategory;

public interface SubcategoryRepo extends JpaRepository<Subcategory, Integer> {
    
}
