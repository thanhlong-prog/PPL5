package com.code.shopee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.code.shopee.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(int id);

    Optional<User> findByGmail(String gmail);

    Optional<User> findByFacebook(String facebook);

    Optional<User> findByPhone(String phone);

    User findByNameAndStatusTrue(String name);

    List<User> findAllByStatusTrueAndEnableTrue();

    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r LEFT JOIN FETCH u.carts WHERE u.status = true AND u.enable = true AND r.role = 'buyer'")
    List<User> findActiveEnabledBuyers();

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.roles r " +
            "LEFT JOIN FETCH u.carts " +
            "LEFT JOIN FETCH u.products p " +
            "LEFT JOIN FETCH u.sellerInfo si " +
            "WHERE u.status = true AND u.enable = true " +
            "AND r.role = 'seller' " +
            "AND (p.status = true OR p IS NULL) " +
            "AND si.isVerify = true")
    List<User> findActiveEnabledSellersWithProducts();

    User findByIdAndStatusTrue(int id);

}
