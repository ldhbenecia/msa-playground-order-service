package com.benecia.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/api/v1/users/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId);
}
