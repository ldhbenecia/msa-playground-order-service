package com.benecia.order_service.service;

import com.benecia.order_service.client.UserClient;
import com.benecia.order_service.dto.CreateOrderRequest;
import com.benecia.order_service.dto.OrderResponse;
import com.benecia.order_service.repository.OrderEntity;
import com.benecia.order_service.repository.OrderJpaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderJpaRepository orderJpaRepository;
    private final UserClient userClient;

    public OrderResponse createOrder(CreateOrderRequest request) {

        // --- 사용자 존재 여부 확인 로직 추가 ---
        try {
            log.info("userId: {} 사용자의 존재 여부 확인 중", request.userId());
            // 5. UserClient를 통해 user-service 호출
            userClient.getUser(request.userId());
            log.info("사용자 찾음: {}", request.userId());
        } catch (FeignException.NotFound ex) {
            // 6. user-service가 404를 반환하면 (사용자 없음) 예외 발생
            log.error("userId: {} 사용자를 찾을 수 없음", request.userId());
            throw new IllegalArgumentException("잘못된 사용자 ID: 사용자가 존재하지 않습니다.");
        } catch (FeignException ex) {
            // 7. 그 외 Feign 통신 오류 처리 (예: user-service 다운)
            log.error("userId: {} 사용자 확인 중 user-service 호출 오류 발생", request.userId(), ex);
            throw new RuntimeException("사용자 존재 여부 확인에 실패했습니다. 잠시 후 다시 시도해주세요.");
        }
        // --- 확인 로직 끝 ---

        OrderEntity orderEntity = new OrderEntity(
                request.productId(),
                request.qty(),
                request.unitPrice(),
                request.userId()
        );

        orderJpaRepository.save(orderEntity);

        return new OrderResponse(
                orderEntity.getId(),
                orderEntity.getProductId(),
                orderEntity.getQty(),
                orderEntity.getUnitPrice(),
                orderEntity.getTotalPrice(),
                orderEntity.getUserId(),
                orderEntity.getCreatedAt()
        );
    }
}
