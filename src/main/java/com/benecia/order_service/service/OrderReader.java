package com.benecia.order_service.service;

import com.benecia.order_service.dto.OrderResponse;
import com.benecia.order_service.repository.OrderEntity;
import com.benecia.order_service.repository.OrderJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReader {

    private final OrderJpaRepository orderJpaRepository;

    public List<OrderResponse> getOrdersByUserId(String userId) {
        List<OrderEntity> orderEntities = orderJpaRepository.findByUserId(userId);

        return orderEntities.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }
}
