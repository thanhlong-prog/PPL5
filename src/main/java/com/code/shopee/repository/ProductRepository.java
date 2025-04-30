package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
    List<Product> findByStatusTrue();
    Page<Product> findByStatusTrue(Pageable pageable); 
    Product findByIdAndStatusTrue(int id); 
}
