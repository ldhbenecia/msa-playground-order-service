package com.benecia.order_service.event;

public record StockFailed(
        Long orderId,
        String userID,
        String reason
) {
}
