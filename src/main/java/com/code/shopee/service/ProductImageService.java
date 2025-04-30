package com.code.shopee.service;

import java.util.List;

import com.code.shopee.model.ProductImage;

public interface ProductImageService {
    public List<ProductImage> getAllProductImageStatusTrue(int productId);
}
