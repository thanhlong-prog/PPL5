package com.code.shopee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.shopee.model.Transaction;
import com.code.shopee.repository.TransactionRepository;
import com.code.shopee.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction findByTraceIdAndStatusTrue(String traceId) {
        return transactionRepository.findByTraceIdAndStatusTrue(traceId);
    }

    @Override
    public Transaction findByVnpTransactionNoAndStatusTrue(String vnpTransactionNo) {
        return transactionRepository.findByVnpTransactionNoAndStatusTrue(vnpTransactionNo);
    }

    @Override
    public Transaction findByOrderIdAndStatusTrue(int orderId) {
        return transactionRepository.findByOrderIdAndStatusTrue(orderId);
    }

    @Override
    public Transaction findByCartIdAndStatusTrue(int cartId) {
        return transactionRepository.findByCartsIdAndStatusTrue(cartId);
    }

    @Override
    public Transaction findByIdAndStatusTrue(int id) {
        return transactionRepository.findByIdAndStatusTrue(id);
    }
    
}
