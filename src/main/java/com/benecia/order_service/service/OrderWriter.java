package com.benecia.order_service.service;

import com.benecia.order_service.dto.CreateOrderRequest;
import com.benecia.order_service.dto.OrderResponse;
import com.benecia.order_service.repository.OrderEntity;
import com.benecia.order_service.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderJpaRepository orderJpaRepository;

    public OrderResponse createOrder(CreateOrderRequest request) {
        OrderEntity orderEntity = new OrderEntity(
                request.productId(),
                request.qty(),
                request.unitPrice(),
                request.userId()
        );

        orderJpaRepository.save(orderEntity);

        return new OrderResponse(
                orderEntity.getId(),
                orderEntity.getProductId(),
                orderEntity.getQty(),
                orderEntity.getUnitPrice(),
                orderEntity.getTotalPrice(),
                orderEntity.getUserId(),
                orderEntity.getCreatedAt()
        );
    }
}
