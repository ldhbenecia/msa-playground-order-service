package com.benecia.order_service.service;

import com.benecia.order_service.common.AppException;
import com.benecia.order_service.common.ErrorCode;
import com.benecia.order_service.dto.CreateOrderRequest;
import com.benecia.order_service.dto.OrderResponse;
import com.benecia.order_service.repository.OrderEntity;
import com.benecia.order_service.repository.OrderJpaRepository;
import com.benecia.order_service.repository.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderJpaRepository orderJpaRepository;

    public OrderResponse createOrder(CreateOrderRequest request) {
        try {
            OrderEntity orderEntity = new OrderEntity(
                    request.productId(),
                    request.qty(),
                    request.unitPrice(),
                    request.userId()
            );

            OrderEntity savedEntity = orderJpaRepository.save(orderEntity);
            log.info("Order saved to DB: {}", orderEntity.getId());

            return OrderResponse.from(savedEntity);
        } catch (Exception e) {
            throw new AppException(ErrorCode.ORDER_FAILED);
        }
    }

    public OrderResponse cancelOrder(Long orderId, String reason) {
        OrderEntity order = orderJpaRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND, "Order not found"));

        if (!order.getStatus().equals(OrderStatus.CANCELLED)) {
            order.cancel(reason);
            log.info("💾 Order status updated to CANCELLED. Id: {}, Reason: {}", orderId, reason);
        } else {
            log.warn("Already CANCELLED. orderId: {}", orderId);
        }

        return OrderResponse.from(order);
    }
}
