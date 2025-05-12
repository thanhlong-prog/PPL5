package com.code.shopee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Transaction findByTraceIdAndStatusTrue(String traceId);
    Transaction findByVnpTransactionNoAndStatusTrue(String vnpTransactionNo);
    Transaction findByOrderIdAndStatusTrue(int orderId);
    Transaction findByCartsIdAndStatusTrue(int cartId);
    Transaction findByIdAndStatusTrue(int id);
}
