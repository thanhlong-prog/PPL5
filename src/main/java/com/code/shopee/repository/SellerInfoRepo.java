package com.code.shopee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.SellerInfo;

public interface  SellerInfoRepo extends JpaRepository<SellerInfo, Integer> {
    
    
}
