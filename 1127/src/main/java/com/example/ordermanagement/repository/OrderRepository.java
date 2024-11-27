package com.example.ordermanagement.repository;

import com.example.ordermanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 根據狀態查詢訂單
    List<Order> findByStatus(String status);

    // 查詢某買家的所有訂單
    List<Order> findByBuyerId(Long buyerId);

    // 查詢某賣家的所有訂單
    List<Order> findBySellerId(Long sellerId);

    // 根據下單時間範圍查詢訂單
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
