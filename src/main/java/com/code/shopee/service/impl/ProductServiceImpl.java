package com.code.shopee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.code.shopee.Config.PagingConfig;
import com.code.shopee.model.Cart;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductOption;
import com.code.shopee.repository.CartRepository;
import com.code.shopee.repository.ProductOptionRepository;
import com.code.shopee.repository.ProductRepository;
import com.code.shopee.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOptionRepository productOptionRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private PagingConfig pagingConfig;

    @Override
    public List<Product> getAllProductStatusTrue() {
        return productRepository.findByStatusTrue();
    }
    @Override
    public Page<Product> getAllProductStatusTrue(int page) {
        Pageable pageable = PageRequest.of(page - 1, pagingConfig.getNumPage());
        return productRepository.findByStatusTrue(pageable);
    }
    @Override
    public Product getProductByIdAndStatusTrue(int id) {
        return productRepository.findByIdAndStatusTrue(id);
    }
    @Override
    public List<ProductOption> getProductOptionByStatusTrue(int productId) {
        return productOptionRepository.findByProductIdAndStatusTrue(productId);   
    }
    @Override
    public List<Cart> getCartByUserIdAndStatusTrue(int userId) {
        return cartRepository.findByUserIdAndStatusTrue(userId);
    }
    @Override
    public void addCart(Cart cart) {
        cartRepository.save(cart);
    }
}
