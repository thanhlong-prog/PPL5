package com.code.shopee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.shopee.model.ProductImage;
import com.code.shopee.repository.ProductImageRepository;
import com.code.shopee.service.ProductImageService;

@Service
public class ProductImageImpl implements ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;
    @Override
    public List<ProductImage> getAllProductImageStatusTrue(int productId) {
        return productImageRepository.findByProductIdAndStatusTrue(productId);
    }
}
