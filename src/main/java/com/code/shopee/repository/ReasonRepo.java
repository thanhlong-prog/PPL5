package com.code.shopee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.Reason;


public interface ReasonRepo extends JpaRepository<Reason, Integer>{
    
}
