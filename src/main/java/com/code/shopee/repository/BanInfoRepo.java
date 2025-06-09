package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.BanInfo;

public interface BanInfoRepo extends JpaRepository<BanInfo, Integer> {
    List<BanInfo> findAllByStatusTrue();
    BanInfo findByUserIdAndStatusTrueAndBanType(int userId, String banType);
}
