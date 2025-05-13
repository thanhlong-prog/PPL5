package com.code.shopee.service;

import com.code.shopee.model.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
    Transaction findByTraceIdAndStatusTrue(String traceId);
    Transaction findByVnpTransactionNoAndStatusTrue(String vnpTransactionNo);
    Transaction findByOrderIdAndStatusTrue(int orderId);
    Transaction findByCartIdAndStatusTrue(int cartId);
    Transaction findByIdAndStatusTrue(int id);
}
