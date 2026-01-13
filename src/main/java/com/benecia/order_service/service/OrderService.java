package com.benecia.order_service.service;

import com.benecia.order_service.dto.CreateOrderRequest;
import com.benecia.order_service.dto.OrderResponse;
import com.benecia.order_service.event.OrderCancelled;
import com.benecia.order_service.event.OrderCreated;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderWriter orderWriter;
    private final OrderReader orderReader;
    private final OrderEventPublisher orderEventPublisher;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        OrderResponse savedOrder = orderWriter.createOrder(request);
        OrderCreated createdEvent = OrderCreated.from(savedOrder);
        orderEventPublisher.publishOrderCreated(createdEvent);
        return savedOrder;
    }

    @Transactional
    public void cancelOrder(Long orderId, String reason) {
        OrderResponse cancelledOrder = orderWriter.cancelOrder(orderId, reason);
        OrderCancelled cancelledEvent = OrderCancelled.from(cancelledOrder);
        orderEventPublisher.publishOrderCancelled(cancelledEvent);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(String userId) {
        return orderReader.getOrdersByUserId(userId);
    }
}
