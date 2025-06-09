package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.shopee.model.Notification;

public interface NotificationRepo extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserIdAndStatusTrue(int userId);
    List<Notification> findByUserIdAndStatusTrueOrderByCreatedDateDesc(Integer userId);
}
