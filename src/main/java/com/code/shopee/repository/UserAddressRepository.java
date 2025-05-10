package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
    List<UserAddress> findByUserIdAndStatusTrue(int userId);
    UserAddress findByIdAndStatusTrue(int id);
    UserAddress findByIsDefaultAndStatusTrue(int isDefault);
}
