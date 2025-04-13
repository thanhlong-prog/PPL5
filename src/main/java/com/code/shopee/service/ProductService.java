package com.code.shopee.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.code.shopee.model.Product;

public interface ProductService {
    public List<Product> getAllProductStatusTrue();
    public Page<Product> getAllProductStatusTrue(int page);
}
