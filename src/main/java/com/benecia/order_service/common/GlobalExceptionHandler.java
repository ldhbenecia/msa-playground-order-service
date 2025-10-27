package com.benecia.order_service.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<OrderErrorResponse> handleAppException(
            AppException ex, HttpServletRequest request) {

        var status = ex.getErrorCode().status();
        var errorResponse = new OrderErrorResponse(
                status,
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
