package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.code.shopee.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserIdAndStatusTrueAndTransactionNull(int userId);

    Cart findByIdAndStatusTrue(int id);

    List<Cart> findByUserIdAndStatusTrueAndTransactionIsNotNullAndShippingStatus(int userId, int shippingStatus);

    List<Cart> findAllByTransactionId(int transactionId);

    @Query("SELECT COUNT(c) FROM Cart c WHERE c.product.seller.id = :sellerId AND c.shippingStatus = :shippingStatus")
    int countBySellerIdAndShippingStatus(@Param("sellerId") int sellerId, @Param("shippingStatus") int shippingStatus);
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.product.seller.id = :sellerId AND transaction IS NOT NULL")
    int countBySellerId(@Param("sellerId") int sellerId);
}
