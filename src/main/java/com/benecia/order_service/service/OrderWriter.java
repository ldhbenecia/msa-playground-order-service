package com.benecia.order_service.service;

import com.benecia.order_service.common.AppException;
import com.benecia.order_service.common.ErrorCode;
import com.benecia.order_service.dto.CreateOrderRequest;
import com.benecia.order_service.dto.OrderResponse;
import com.benecia.order_service.event.OrderCancelled;
import com.benecia.order_service.event.OrderCreated;
import com.benecia.order_service.repository.OrderEntity;
import com.benecia.order_service.repository.OrderJpaRepository;
import com.benecia.order_service.repository.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderProducer orderProducer;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        try {
            OrderEntity orderEntity = new OrderEntity(
                    request.productId(),
                    request.qty(),
                    request.unitPrice(),
                    request.userId()
            );

            orderJpaRepository.save(orderEntity);
            log.info("Order saved to DB: {}", orderEntity.getId());

            OrderCreated createdOrder = OrderCreated.from(orderEntity);
            orderProducer.sendOrderCreated(createdOrder);

            return new OrderResponse(
                    orderEntity.getId(),
                    orderEntity.getProductId(),
                    orderEntity.getQty(),
                    orderEntity.getUnitPrice(),
                    orderEntity.getTotalPrice(),
                    orderEntity.getUserId(),
                    orderEntity.getCreatedAt()
            );
        } catch (Exception e) {
            throw new AppException(ErrorCode.ORDER_FAILED);
        }
    }

    @Transactional
    public void cancelOrderAndBroadcast(Long orderId, String reason) {
        OrderEntity order = orderJpaRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND, "Order not found"));

        if (order.getStatus().equals(OrderStatus.CANCELLED)) {
            log.warn("Already CANCELLED. orderId: {}", orderId);
            return;
        }

        order.cancel(reason);
        log.info("Order CANCELLED. Id: {}, Reason: {}", orderId, reason);

        // 취소 방송 송출 (Product는 재고 복구, User는 포인트 환불 수행)
        OrderCancelled cancelledDto = new OrderCancelled(
                order.getId(),
                order.getProductId(),
                order.getQty(),
                order.getUserId(),
                order.getTotalPrice()
        );
        orderProducer.sendOrderCancelled(cancelledDto);
    }
}
