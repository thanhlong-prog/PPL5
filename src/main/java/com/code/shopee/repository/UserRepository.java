package com.code.shopee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    User findByUsername(String username);
}
