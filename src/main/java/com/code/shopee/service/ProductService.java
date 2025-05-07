package com.code.shopee.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.code.shopee.model.Cart;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductOption;

public interface ProductService {
    public List<Product> getAllProductStatusTrue();
    public Page<Product> getAllProductStatusTrue(int page);
    public Product getProductByIdAndStatusTrue(int id);
    public List<ProductOption> getProductOptionByStatusTrue(int productId);
    public List<Cart> getCartByUserIdAndStatusTrue(int userId);
    public void addCart(Cart cart);
    public void deleteCart(int cartId);
    public Cart getCartByIdAndStatusTrue(int id);
}
