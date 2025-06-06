package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.code.shopee.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Transaction findByTraceIdAndStatusTrue(String traceId);

    Transaction findByVnpTransactionNoAndStatusTrue(String vnpTransactionNo);

    Transaction findByOrderIdAndStatusTrue(int orderId);

    Transaction findByCartsIdAndStatusTrue(int cartId);

    Transaction findByIdAndStatusTrue(int id);

    Transaction findByTxnRefAndStatusFalse(String txnRef);

    @Query("SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.carts")
    List<Transaction> findAllWithCarts();

    @Query("SELECT DISTINCT t FROM Transaction t " +
            "JOIN FETCH t.carts c " +
            "JOIN c.product p " +
            "WHERE p.seller.id = :sellerId")
    List<Transaction> findAllBySellerId(@Param("sellerId") int sellerId);

    @Query("SELECT DISTINCT t FROM Transaction t " +
            "JOIN t.carts c " +
            "WHERE c.product.seller.id = :sellerId " +
            "AND (:shippingStatus IS NULL OR c.shippingStatus = :shippingStatus)")
    List<Transaction> findAllBySellerIdAndShippingStatus(
            @Param("sellerId") int sellerId,
            @Param("shippingStatus") Integer shippingStatus);

}
