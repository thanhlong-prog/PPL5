package com.code.shopee.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.code.shopee.model.Cart;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductOption;
import com.code.shopee.model.ProductOptionValues;
import com.code.shopee.model.ProductOptions;
import com.code.shopee.model.ProductVatiants;

public interface ProductService {
    public List<Product> getAllProductStatusTrue();
    public Page<Product> getAllProductStatusTrue(int page);
    public Product getProductByIdAndStatusTrue(int id);
    public List<ProductOption> getProductOptionByStatusTrue(int productId);
    public List<Cart> getCartByUserIdAndStatusTrue(int userId);
    public void addCart(Cart cart);
    public void deleteCart(int cartId);
    public Cart getCartByIdAndStatusTrue(int id);

    public List<ProductOptions> getAllOptionsByStatusTrue(int productId);
    public List<ProductOptionValues> getAllOptionValuesByStatusTrue(int optionId);
    Map<ProductOptions, List<ProductOptionValues>> getOptionWithValuesByStatusTrue(int productId);
    public ProductVatiants getProductVatiantsByStatusTrue(int productId);
    List<ProductVatiants> getAllProductVatiantsByStatusTrue(int productId);
    public int getQuantityByOptions(int productId, Map<String, String> selectedOptions);
    public ProductVatiants getVatiantByOptions(int productId, Map<String, String> selectedOptions);

    public List<Cart> getAllCartWaitingForShip(int userId, int shippingStatus);
    public List<Product> getProductBySubcateId(int subcategoryId);
}
