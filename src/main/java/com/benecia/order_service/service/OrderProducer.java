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
public class OrderProducer {

    private final StreamBridge streamBridge;

    public void sendOrderCreated(OrderCreated createdDto) {
        try {
            log.info("Sending 'order-created' event: {}", createdDto.orderId());

            Message<OrderCreated> message = MessageBuilder
                    .withPayload(createdDto)
                    .setHeader(KafkaHeaders.KEY, createdDto.productId().getBytes())
                    .build();

            streamBridge.send("orderCreated-out-0", message);
        } catch (Exception e) {
            log.error("Failed to send order-created event", e);
        }
    }

    public void sendOrderCancelled(OrderCancelled cancelledDto) {
        try {
            log.info("Sending 'order-cancelled' event for recovery: {}", cancelledDto.orderId());

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
