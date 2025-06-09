package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.code.shopee.model.Product;
import com.code.shopee.model.User;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByStatusTrue();

    Page<Product> findByStatusTrue(Pageable pageable);

    Product findByIdAndStatusTrue(int id);

    List<Product> findBySubcategoryIdAndStatusTrue(int subcategoryId);

    List<Product> findBySellerIdAndStatusTrue(int sellerId);

    List<Product> findBySellerId(int sellerId);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.productVatiants pv WHERE pv.createdBy = :user AND p.seller.id = :sellerid")
    List<Product> findProductsAndSellerIdByProductVatiantsCreatedBy(int sellerid, @Param("user") User user);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.productVatiants pv WHERE pv.createdBy = :user AND p.status = true AND p.seller.id = :sellerid")
    List<Product> findActiveProductsAndSellerIdAndByProductVatiantsCreatedBy(int sellerid, @Param("user") User user);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.productVatiants pv " +
            "WHERE pv.createdBy = :user " +
            "AND p.status = false " +
            "AND p.seller.id = :sellerid " +
            "AND p.isBan = false")
    List<Product> findInactiveAndNotBannedProductsBySellerAndCreatedBy(
            @Param("sellerid") int sellerid,
            @Param("user") User user);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.productVatiants pv " +
            "WHERE pv.createdBy = :user " +
            "AND p.status = false " +
            "AND p.seller.id = :sellerid " +
            "AND p.isBan = true")
    List<Product> findInactiveProductsAndSellerIdByProductVatiantsCreatedByAndBanned(
            @Param("sellerid") int sellerid,
            @Param("user") User user);

    int countBySellerIdAndStatusTrue(int sellerId);

    int countBySellerIdAndStatusFalseAndIsBanFalse(int sellerId);
    
    int countBySellerId(int sellerId);

    int countBySellerIdAndStatusFalseAndIsBanTrue(int sellerId);

    int countByStatusTrue();

    int countByStatusFalse();

    int countByStatusFalseAndIsBanFalse();

    int countByStatusFalseAndIsBanTrue();


    List<Product> findByStatusFalseAndIsBanFalse();

    List<Product> findByStatusFalseAndIsBanTrue();
}
