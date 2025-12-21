package com.benecia.order_service.event;

import com.benecia.order_service.repository.OrderEntity;
import java.time.LocalDateTime;

public record OrderCreated(
        Long orderId,
        String productId,
        Integer qty,
        Integer unitPrice,
        Integer totalPrice,
        String userId,
        LocalDateTime createdAt
) {

    public static OrderCreated from(OrderEntity orderEntity) {
        return new OrderCreated(
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
