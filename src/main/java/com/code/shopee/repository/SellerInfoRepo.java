package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.SellerInfo;

public interface  SellerInfoRepo extends JpaRepository<SellerInfo, Integer> {
    List<SellerInfo> findAll();
    SellerInfo findByUserId(int userId);
}
