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
public class KafkaEventConsumer {

    private final OrderWriter orderWriter;

    // User 실패 -> 재고 복구 필요 -> 방송
    @Bean
    public Consumer<PointsFailed> pointsFailed() {
        return failedDto -> {
            log.warn("Received points-failed: {}. Cancelling order & Broadcasting.", failedDto);
            orderWriter.cancelOrderAndBroadcast(failedDto.orderId(), failedDto.reason());
        };
    }

    // Product 실패 -> 포인트 환불 필요 -> 방송
    @Bean
    public Consumer<StockFailed> stockFailed() {
        return failedDto -> {
            log.warn("Received stock-failed: {}. Cancelling order & Broadcasting.", failedDto);
            orderWriter.cancelOrderAndBroadcast(failedDto.orderId(), failedDto.reason());
        };
    }
}
