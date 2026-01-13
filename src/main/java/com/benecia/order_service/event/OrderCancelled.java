package com.benecia.order_service.event;

import com.benecia.order_service.dto.OrderResponse;

public record OrderCancelled(
        Long orderId,
        String productId,
        Integer qty,
        String userId,
        Integer totalPrice
) {

    public static OrderCancelled from(OrderResponse response) {
        return new OrderCancelled(
                response.orderId(),
                response.productId(),
                response.qty(),
                response.userId(),
                response.totalPrice()
        );
    }
}
