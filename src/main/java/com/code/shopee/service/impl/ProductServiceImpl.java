package com.code.shopee.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.code.shopee.Config.PagingConfig;
import com.code.shopee.model.Cart;
import com.code.shopee.model.Product;
import com.code.shopee.model.ProductOption;
import com.code.shopee.model.ProductOptionValues;
import com.code.shopee.model.ProductOptions;
import com.code.shopee.model.ProductVatiants;
import com.code.shopee.model.Subcategory;
import com.code.shopee.repository.CartRepository;
import com.code.shopee.repository.ProductOptionRepository;
import com.code.shopee.repository.ProductOptionValuesRepo;
import com.code.shopee.repository.ProductOptionsReposioty;
import com.code.shopee.repository.ProductRepository;
import com.code.shopee.repository.ProductVatiantsRepo;
import com.code.shopee.repository.SubcategoryRepo;
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
    @Autowired
    private ProductOptionsReposioty productOptionsReposioty;
    @Autowired
    private ProductOptionValuesRepo productOptionValues;
    @Autowired
    private ProductVatiantsRepo productVatiantsRepo;
    @Autowired
    private SubcategoryRepo subcategoryRepository;

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
        return cartRepository.findByUserIdAndStatusTrueAndTransactionNull(userId);
    }

    @Override
    public void addCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public void deleteCart(int cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public Cart getCartByIdAndStatusTrue(int id) {
        return cartRepository.findByIdAndStatusTrue(id);
    }

    @Override
    public List<ProductOptions> getAllOptionsByStatusTrue(int productId) {
        return productOptionsReposioty.findByProductIdAndStatusTrueOrderByIdAsc(productId);
    }

    @Override
    public List<ProductOptionValues> getAllOptionValuesByStatusTrue(int optionId) {
        return productOptionValues.findByOptionIdAndStatusTrueOrderByIdAsc(optionId);
    }

    @Override
    public Map<ProductOptions, List<ProductOptionValues>> getOptionWithValuesByStatusTrue(int productId) {
        List<ProductOptions> options = productOptionsReposioty.findByProductIdAndStatusTrueOrderByIdAsc(productId);
        Map<ProductOptions, List<ProductOptionValues>> result = new LinkedHashMap<>();

        for (ProductOptions option : options) {
            List<ProductOptionValues> values = productOptionValues
                    .findByOptionIdAndStatusTrueOrderByIdAsc(option.getId());
            result.put(option, values);
        }

        return result;
    }

    @Override
    public ProductVatiants getProductVatiantsByStatusTrue(int productId) {
        return productVatiantsRepo.findByProductIdAndStatusTrue(productId);
    }

    @Override
    public List<ProductVatiants> getAllProductVatiantsByStatusTrue(int productId) {
        return productVatiantsRepo.findByProductIdAndStatusTrueOrderByIdAsc(productId);
    }

    @Override
    public int getQuantityByOptions(int productId, Map<String, String> selectedOptions) {
        List<ProductVatiants> variants = productVatiantsRepo.findByProductIdAndStatusTrueOrderByIdAsc(productId);

        for (ProductVatiants variant : variants) {
            Set<ProductOptionValues> values = variant.getOptionValues();

            boolean match = selectedOptions.entrySet().stream().allMatch(
                    entry -> values.stream().anyMatch(v -> v.getOption().getName().equalsIgnoreCase(entry.getKey()) &&
                            v.getValue().equalsIgnoreCase(entry.getValue())));

            if (match && values.size() == selectedOptions.size()) {
                return variant.getQuantity();
            }
        }

        return 0;
    }

    @Override
    public int getPriceByOptions(int productId, Map<String, String> selectedOptions) {
        List<ProductVatiants> variants = productVatiantsRepo.findByProductIdAndStatusTrueOrderByIdAsc(productId);

        for (ProductVatiants variant : variants) {
            Set<ProductOptionValues> values = variant.getOptionValues();

            boolean match = selectedOptions.entrySet().stream().allMatch(
                    entry -> values.stream().anyMatch(v -> v.getOption().getName().equalsIgnoreCase(entry.getKey()) &&
                            v.getValue().equalsIgnoreCase(entry.getValue())));

            if (match && values.size() == selectedOptions.size()) {
                return variant.getPrice();
            }
        }

        return 0;
    }

    @Override
    public ProductVatiants getVatiantByOptions(int productId, Map<String, String> selectedOptions) {
        List<ProductVatiants> variants = productVatiantsRepo.findByProductIdAndStatusTrueOrderByIdAsc(productId);

        for (ProductVatiants variant : variants) {
            Set<ProductOptionValues> values = variant.getOptionValues();

            boolean match = selectedOptions.entrySet().stream().allMatch(
                    entry -> values.stream().anyMatch(v -> v.getOption().getName().equalsIgnoreCase(entry.getKey()) &&
                            v.getValue().equalsIgnoreCase(entry.getValue())));

            if (match && values.size() == selectedOptions.size()) {
                return variant;
            }
        }

        return null;
    }

    @Override
    public List<Cart> getAllCartWaitingForShip(int userId, int shippingStatus) {
        return cartRepository.findByUserIdAndStatusTrueAndTransactionIsNotNullAndShippingStatus(userId, shippingStatus);
    }

    @Override
    public List<Product> getProductBySubcateId(int subcategoryId) {
        return productRepository.findBySubcategoryIdAndStatusTrue(subcategoryId);
    }

    @Override
    public List<Product> getProductBySeller(int sellerId) {
        return productRepository.findBySellerIdAndStatusTrue(sellerId);
    }

    @Override
    public List<Subcategory> getSubcategoriesByIds(Set<Integer> ids) {
        return subcategoryRepository.findAllById(ids); // nếu dùng JPA
    }

}
