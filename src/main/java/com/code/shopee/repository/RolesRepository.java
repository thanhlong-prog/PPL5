package com.code.shopee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.Roles;

public interface  RolesRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findById(int id);
    Optional<Roles> findByRole(String role);
}
