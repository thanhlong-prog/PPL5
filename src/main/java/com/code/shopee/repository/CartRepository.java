package com.code.shopee.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.code.shopee.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
        List<Cart> findByUserIdAndStatusTrueAndTransactionNull(int userId);

        Cart findByIdAndStatusTrue(int id);

        List<Cart> findByUserIdAndStatusTrueAndTransactionIsNotNull(int userId);

        List<Cart> findByUserIdAndStatusTrueAndTransactionIsNotNullAndShippingStatus(int userId, int shippingStatus);

        List<Cart> findAllByTransactionId(int transactionId);

        @Query("SELECT COUNT(c) FROM Cart c WHERE c.product.seller.id = :sellerId AND c.shippingStatus = :shippingStatus")
        int countBySellerIdAndShippingStatus(@Param("sellerId") int sellerId,
                        @Param("shippingStatus") int shippingStatus);

        @Query("SELECT COUNT(c) FROM Cart c WHERE c.product.seller.id = :sellerId AND transaction IS NOT NULL")
        int countBySellerId(@Param("sellerId") int sellerId);

        @Query("SELECT c FROM Cart c " +
                        "JOIN c.product p " +
                        "JOIN c.transaction t " +
                        "WHERE p.seller.id = :sellerId " +
                        "AND t.createdDate BETWEEN :startDate AND :endDate")
        List<Cart> findBySellerIdAndTransactionCreatedDateBetween(
                        @Param("sellerId") int sellerId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query("SELECT c FROM Cart c " +
                        "JOIN c.product p " +
                        "JOIN c.transaction t " +
                        "WHERE p.seller.id = :sellerId " +
                        "AND FUNCTION('YEAR', t.createdDate) = :year " +
                        "AND FUNCTION('MONTH', t.createdDate) = :month")
        List<Cart> findBySellerIdAndTransactionCreatedYearMonth(
                        @Param("sellerId") int sellerId,
                        @Param("year") int year,
                        @Param("month") int month);

        @Query("SELECT c FROM Cart c " +
                        "JOIN c.product p " +
                        "JOIN c.transaction t " +
                        "WHERE p.seller.id = :sellerId " +
                        "AND FUNCTION('YEAR', t.createdDate) = :year " +
                        "AND FUNCTION('QUARTER', t.createdDate) = :quarter")
        List<Cart> findBySellerIdAndTransactionCreatedYearQuarter(
                        @Param("sellerId") int sellerId,
                        @Param("year") int year,
                        @Param("quarter") int quarter);

        @Query("SELECT c FROM Cart c " +
                        "JOIN c.product p " +
                        "WHERE p.seller.id = :sellerId " +
                        "AND c.status = 1 " +
                        "AND c.shippingStatus = 2")
        List<Cart> findBySellerIdAndStatusTrueAndShippingStatus2(@Param("sellerId") int sellerId);

        @Query("SELECT COUNT(c) FROM Cart c WHERE c.status = 1 AND c.transaction IS NOT NULL")
        int countByStatusTrueAndTransactionNotNull();

        @Query("SELECT c FROM Cart c WHERE c.status = 1 AND c.transaction IS NOT NULL")
        List<Cart> findByStatusTrueAndTransactionNotNull();

}
