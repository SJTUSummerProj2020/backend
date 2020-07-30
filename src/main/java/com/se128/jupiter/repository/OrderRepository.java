package com.se128.jupiter.repository;

import com.se128.jupiter.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findOrdersByUserId(Integer userId);
}
