package com.code.shopee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.Reason;
import com.code.shopee.model.Transaction;


public interface ReasonRepo extends JpaRepository<Reason, Integer>{
     boolean existsByTransaction(Transaction transaction);
}
