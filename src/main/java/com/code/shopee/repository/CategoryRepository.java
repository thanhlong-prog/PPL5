package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.Category;


public interface  CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByStatusTrue();
}

