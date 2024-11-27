package com.example.ordermanagement.controller;

import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 訂單管理頁面
    @GetMapping("/order-management")
    public String adminOrderManagement() {
        return "adminOrderManagement"; // 對應 resources/templates/adminOrderManagement.html
    }

    // 返回 JSON 的方法，顯式加上 @ResponseBody
    @ResponseBody
    @GetMapping
    public List<Order> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long buyerId,
            @RequestParam(required = false) Long sellerId) {
        if (status != null) {
            return orderService.getOrdersByStatus(status);
        } else if (buyerId != null) {
            return orderService.getOrdersByBuyerId(buyerId);
        } else if (sellerId != null) {
            return orderService.getOrdersBySellerId(sellerId);
        }
        return orderService.getAllOrders();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Map<String, Object> updatedFields) {
        if (updatedFields.containsKey("status")) {
            String newStatus = (String) updatedFields.get("status");
            boolean updated = orderService.updateOrderAndShipping(id, newStatus);
            return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        }

        return ResponseEntity.badRequest().body("No valid fields to update");
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
