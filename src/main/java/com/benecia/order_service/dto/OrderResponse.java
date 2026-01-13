package com.benecia.order_service.dto;

import com.benecia.order_service.repository.OrderEntity;
import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        String productId,
        Integer qty,
        Integer unitPrice,
        Integer totalPrice,
        String userId,
        LocalDateTime createdAt
) {

    public static OrderResponse from(OrderEntity entity) {
        return new OrderResponse(
                entity.getId(),
                entity.getProductId(),
                entity.getQty(),
                entity.getUnitPrice(),
                entity.getTotalPrice(),
                entity.getUserId(),
                entity.getCreatedAt()
        );
    }
}
