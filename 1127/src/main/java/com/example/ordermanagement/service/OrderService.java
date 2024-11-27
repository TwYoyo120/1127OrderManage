package com.example.ordermanagement.service;

import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.model.ShippingInfo;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.repository.ShippingInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    // 獲取所有訂單
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 根據狀態篩選訂單
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    // 根據買家ID篩選訂單
    public List<Order> getOrdersByBuyerId(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    // 根據賣家ID篩選訂單
    public List<Order> getOrdersBySellerId(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    // 根據ID獲取訂單詳情
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // 更新訂單並同步物流信息狀態
    public boolean updateOrderAndShipping(Long orderId, String newStatus) {
        return orderRepository.findById(orderId).map(order -> {
            // 更新訂單狀態
            order.setStatus(newStatus);
            orderRepository.save(order);

            // 同步更新物流信息狀態
            List<ShippingInfo> shippingInfos = shippingInfoRepository.findByOrderId(orderId);
            for (ShippingInfo shippingInfo : shippingInfos) {
                shippingInfo.setStatus(newStatus);
                shippingInfoRepository.save(shippingInfo);
            }
            return true;
        }).orElse(false);
    }


    // 刪除訂單
    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
