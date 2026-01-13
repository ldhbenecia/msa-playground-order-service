package com.benecia.order_service.service;

import com.benecia.order_service.event.OrderCancelled;
import com.benecia.order_service.event.OrderCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final StreamBridge streamBridge;

    public void publishOrderCreated(OrderCreated createdDto) {
        try {
            log.info("📢 Publishing 'order-created' event: {}", createdDto.orderId());

            Message<OrderCreated> message = MessageBuilder
                    .withPayload(createdDto)
                    .setHeader(KafkaHeaders.KEY, createdDto.productId().getBytes())
                    .build();

            streamBridge.send("orderCreated-out-0", message);
        } catch (Exception e) {
            log.error("Failed to send order-created event", e);
        }
    }

    public void publishOrderCancelled(OrderCancelled cancelledDto) {
        try {
            log.info("📢 Publishing 'order-cancelled' event: {}", cancelledDto.orderId());

            Message<OrderCancelled> message = MessageBuilder
                    .withPayload(cancelledDto)
                    .setHeader(KafkaHeaders.KEY, cancelledDto.productId().getBytes())
                    .build();

            streamBridge.send("orderCancelled-out-0", message);
        } catch (Exception e) {
            log.error("Failed to send order-cancelled event", e);
        }
    }
}
