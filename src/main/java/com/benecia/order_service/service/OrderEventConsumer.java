package com.benecia.order_service.service;

import com.benecia.order_service.event.PointsFailed;
import com.benecia.order_service.event.StockFailed;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderService orderService;

    // User 서비스 실패 시 (보상 트랜잭션)
    @Bean
    public Consumer<PointsFailed> pointsFailed() {
        return failedDto -> {
            log.warn("🚨 Received points-failed: {}. Triggering compensation transaction.", failedDto);
            orderService.cancelOrder(failedDto.orderId(), failedDto.reason());
        };
    }

    // Product 서비스 실패 시 (보상 트랜잭션)
    @Bean
    public Consumer<StockFailed> stockFailed() {
        return failedDto -> {
            log.warn("🚨 Received stock-failed: {}. Triggering compensation transaction.", failedDto);
            orderService.cancelOrder(failedDto.orderId(), failedDto.reason());
        };
    }
}
