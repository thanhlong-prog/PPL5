package com.code.shopee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.code.shopee.model.Product;
import com.code.shopee.repository.ProductRepository;
import com.code.shopee.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProductStatusTrue() {
        return productRepository.findByStatusTrue();
    }
    @Override
    public Page<Product> getAllProductStatusTrue(int page) {
        Pageable pageable = PageRequest.of(page - 1, 2);
        return productRepository.findByStatusTrue(pageable);
    }
}
