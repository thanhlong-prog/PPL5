package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserIdAndStatusTrueAndTransactionNull(int userId);
    Cart findByIdAndStatusTrue(int id);
    List<Cart> findByUserIdAndStatusTrueAndTransactionIsNotNullAndShippingStatus(int userId, int shippingStatus);
    List<Cart> findAllByTransactionId(int transactionId);
}
