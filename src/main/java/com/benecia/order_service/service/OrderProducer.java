package com.benecia.order_service.service;

import com.benecia.order_service.event.OrderCancelled;
import com.benecia.order_service.event.OrderCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final StreamBridge streamBridge;

    public void sendOrderCreated(OrderCreated createdDto) {
        try {
            log.info("Sending 'order-created' event: {}", createdDto.orderId());
            streamBridge.send("orderCreated-out-0", createdDto);
        } catch (Exception e) {
            log.error("Failed to send order-created event", e);
        }
    }

    public void sendOrderCancelled(OrderCancelled cancelledDto) {
        try {
            log.info("Sending 'order-cancelled' event for recovery: {}", cancelledDto.orderId());
            streamBridge.send("order-cancelled-out-0", MessageBuilder.withPayload(cancelledDto).build());
        } catch (Exception e) {
            log.error("Failed to send order-cancelled event", e);
        }
    }
}
