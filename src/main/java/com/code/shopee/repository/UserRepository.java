package com.code.shopee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByUsername(String username);
    Optional<User> findById(int id);
    Optional<User> findByGmail(String gmail);
    Optional<User> findByFacebook(String facebook);
}
