package com.benecia.order_service.event;

import com.benecia.order_service.dto.OrderResponse;
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

    public static OrderCreated from(OrderResponse response) {
        return new OrderCreated(
                response.orderId(),
                response.productId(),
                response.qty(),
                response.unitPrice(),
                response.totalPrice(),
                response.userId(),
                response.createdAt()
        );
    }
}
