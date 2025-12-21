package com.benecia.order_service.event;

public record PointsFailed(
        Long orderId,
        String userId,
        String reason
) {
}
