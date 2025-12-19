package com.benecia.order_service.dto;

public record StockFailed(
        Long orderId,
        String userID,
        String reason
) {
}
